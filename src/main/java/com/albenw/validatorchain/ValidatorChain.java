package com.albenw.validatorchain;

import com.albenw.validatorchain.base.Validator;
import com.albenw.validatorchain.base.ValidatorContext;
import com.albenw.validatorchain.base.ValidatorResult;
import com.albenw.validatorchain.base.ValidatorUnit;
import com.albenw.validatorchain.factory.SimpleValidatorFactory;
import com.albenw.validatorchain.factory.ValidatorFactory;
import com.albenw.validatorchain.util.CollectionUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

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
     * 校验对象和校验器
     */
    private List<ValidatorUnit> validatorUnits = new LinkedList<>();

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

    public <T, E extends Validator> ValidatorChain addLast(T target, Class<E> validatorType) throws Exception{
        if(validatorType == null){
            throw new NullPointerException("validatorType can not be null");
        }
        if(!checkDuplicate(target, validatorType)){
            Validator validator = ValidatorCache.getByType(this.validatorFactory, validatorType);
            validatorUnits.add(new ValidatorUnit<T>(target, validator));
        }
        return this;
    }

    public <T> ValidatorChain addLast(T target, Validator<T> validatorInstance) {
        if(validatorInstance == null){
            throw new NullPointerException("validatorInstance can not be null");
        }
        if(!checkDuplicate(target, validatorInstance.getClass())){
            validatorUnits.add(new ValidatorUnit<T>(target, validatorInstance));
        }
        return this;
    }

    private boolean checkDuplicate(Object target, Class type){
        for(ValidatorUnit unit : validatorUnits) {
            if (unit.getTarget().toString().equals(target.toString()) && unit.getValidator().getClass().getName().equals(type.getName())) {
                return true;
            }
        }
        return false;
    }

    public <E extends ValidatorCallback> ValidatorChain callback(E callback){
        if(callback == null){
            throw new NullPointerException("callback can not be null");
        }
        this.callback = callback;
        return this;
    }

    @SuppressWarnings("unchecked")
    public ValidatorChain doValidate() throws Exception {
        if(CollectionUtils.isEmpty(validatorUnits)){
            logger.warn("not any validators configured");
            return this;
        }
        ValidatorResult result = new ValidatorResult();
        this.result = result;
        context.setResult(result);
        for(ValidatorUnit unit : validatorUnits){
            Object target = unit.getTarget();
            Validator validator = unit.getValidator();
            try{
                if(!validator.accept(target, context)){
                    continue;
                }
                if(!validator.validate(target, context)){
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
                    callback.onUncaughtException(validatorUnits, context, e);
                }catch (Exception e2){
                    logger.error("validate chain exception in the course of exception", e);
                    throw e;
                }
            }
        }
        if(result.getIsSuccess()){
            callback.onSuccess(validatorUnits, context);
        }else{
            callback.onFail(validatorUnits, context);
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

}
