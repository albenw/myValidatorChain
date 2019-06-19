package com.albenw.validatorchain;

import com.albenw.validatorchain.base.SceneProvider;
import com.albenw.validatorchain.base.Validator;
import com.albenw.validatorchain.base.ValidatorContext;
import com.albenw.validatorchain.base.ValidatorResult;
import com.albenw.validatorchain.factory.SimpleValidatorFactory;
import com.albenw.validatorchain.factory.ValidatorFactory;
import com.albenw.validatorchain.util.CollectionUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author alben.wong
 */
@Getter @Setter
public class ValidatorChain {

    private Logger logger = LoggerFactory.getLogger(ValidatorChain.class);

    /**
     * 是否失败就返回
     */
    private boolean isFailFast = true;

    /**
     * 校验的上下文
     */
    private ValidatorContext context = new ValidatorContext();

    /**
     * 校验的对象
     */
    Object target;

    /**
     * 生成validator的工厂
     */
    private ValidatorFactory validatorFactory = new SimpleValidatorFactory();

    /**
     * 校验的回调
     */
    private ValidatorCallback callback = new DefaultValidateCallback();

    /**
     * 校验结果
     */
    private ValidatorResult result;

    /**
     * 全部场景都需要的校验器
     * <validator-class-name>
     */
    private List<String> allSceneValidators = new ArrayList<>();

    /**
     * 场景与校验器的映射
     * <scene-name, validator-class-name>
     */
    private Map<String, List<String>> sceneValidatorMap = new ConcurrentHashMap<>();

    /**
     * 校验器类名与校验器的映射
     * <validator-class-name, validator-instance>
     */
    private Map<String, Validator> validators = new ConcurrentHashMap<>();

    /**
     * 场景提供器
     */
    private SceneProvider sceneProvider;


    private ValidatorChain() {

    }

    public static ValidatorChain build(){
        return new ValidatorChain();
    }

    public ValidatorChain failFast(boolean val){
        this.isFailFast = val;
        return this;
    }

    public ValidatorChain useFactory(ValidatorFactory factory){
        this.validatorFactory = factory;
        return this;
    }

    public ValidatorChain target(Object target){
        this.target = target;
        return this;
    }

    public <E extends Validator> ValidatorChain addLast(Class<E> validatorType) throws Exception {
        if(validatorType == null){
            throw new NullPointerException("validatorType can not be null");
        }
        Validator validator = ValidatorCache.getByType(this.validatorFactory, validatorType);
        addToAllSceneValidator(validatorType.getName());
        validators.put(validatorType.getName(), validator);
        return this;
    }

    public <E extends Validator> ValidatorChain addLast(Class<E> validatorType, List<String> scenes) throws Exception{
        if(validatorType == null){
            throw new NullPointerException("validatorType can not be null");
        }
        if(CollectionUtils.isEmpty(scenes)){
            throw new NullPointerException("scenes can not be empty");
        }
        Validator validator = ValidatorCache.getByType(this.validatorFactory, validatorType);
        validators.put(validatorType.getName(), validator);
        for(String scene : scenes){
            addScene(scene, validator);
        }
        return this;
    }

    public <T> ValidatorChain addLast(Validator<T> validatorInstance) {
        if(validatorInstance == null){
            throw new NullPointerException("validatorInstance can not be null");
        }
        allSceneValidators.add(validatorInstance.getClass().getName());
        validators.put(validatorInstance.getClass().getName(), validatorInstance);
        return this;
    }

    public <T> ValidatorChain addLast(Validator<T> validatorInstance, List<String> scenes) {
        if(validatorInstance == null){
            throw new NullPointerException("validatorInstance can not be null");
        }
        if(CollectionUtils.isEmpty(scenes)){
            throw new NullPointerException("scenes can not be empty");
        }
        validators.put(validatorInstance.getClass().getName(), validatorInstance);
        for(String scene : scenes){
            addScene(scene, validatorInstance);
        }
        return this;
    }

    public <E extends ValidatorCallback> ValidatorChain callback(E callback){
        if(callback == null){
            throw new NullPointerException("callback can not be null");
        }
        this.callback = callback;
        return this;
    }

    public ValidatorChain sceneProvider(SceneProvider sceneProvider){
        this.sceneProvider = sceneProvider;
        return this;
    }

    @SuppressWarnings("unchecked")
    public ValidatorChain doValidate() throws Exception {
        this.checkArgs();
        ValidatorResult result = new ValidatorResult();
        this.result = result;
        context.setResult(result);
        List<String> scenes = this.sceneProvider.decide(target);
        List<Validator> dealValidators = getDealValidators(scenes);
        for(Validator validator : dealValidators){
            try{
                if(!validator.accept(target ,context)){
                    continue;
                }
                if(!validator.validate(target ,context)){
                    result.setIsSuccess(false);
                    if(this.isFailFast){
                        break;
                    }
                }
            }catch (Exception e) {
                logger.warn("validate chain exception occur, continue to deal with it");
                result.setIsSuccess(false);
                try{
                    validator.onException(target, context, e);
                    callback.onException(target, dealValidators, context, e);
                }catch (Exception e2){
                    logger.error("validate chain exception in the course of exception", e);
                    throw e;
                }
            }
        }
        if(result.getIsSuccess()){
            callback.onSuccess(target, dealValidators, context);
        }else{
            callback.onFail(target, dealValidators, context);
        }
        return this;
    }

    public void result(BiConsumer<String, List<String>> biConsumer) {
        if(this.result == null){
            throw new NullPointerException("do validate before get the result");
        }
        for(Map.Entry<String, List<String>> entry : result.getErrors().entrySet()){
            biConsumer.accept(entry.getKey(), entry.getValue());
        }
    }

    public ValidatorChain setAttribute(String key, Object obj){
        context.addAttribute(key, obj);
        return this;
    }

    private void addScene(String scene, Validator validator){
        if(!sceneValidatorMap.containsKey(scene)){
            sceneValidatorMap.put(scene, new ArrayList<>());
        }
        List<String> sceneValidators = sceneValidatorMap.get(scene);
        if(!sceneValidators.contains(validator.getClass().getName())){
            sceneValidators.add(validator.getClass().getName());
        }
    }

    private void checkArgs(){
        if(target == null){
            throw new NullPointerException("target can not be null");
        }
        if(validators.isEmpty()){
            throw new NullPointerException("there is no any validators, please use addLast to add one");
        }
        if(sceneProvider == null){
            throw new NullPointerException("sceneProvider can not be null");
        }
    }

    private void addToAllSceneValidator(String validatorTypeName){
        if(!allSceneValidators.contains(validatorTypeName)){
            allSceneValidators.add(validatorTypeName);
        }
    }

    private List<Validator> getDealValidators(List<String> scenes){
        //处理全部的场景
        List<String> dealValidators = new ArrayList<>(allSceneValidators);
        for(String scene : scenes){
            if(sceneValidatorMap.containsKey(scene)){
                dealValidators.addAll(sceneValidatorMap.get(scene));
            }
        }
        return dealValidators.stream().map(validators::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
