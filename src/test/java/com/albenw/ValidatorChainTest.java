package com.albenw;

import com.albenw.validatorchain.*;
import com.albenw.validatorchain.base.ValidatorContext;
import com.albenw.validatorchain.factory.SpringValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;


/**
 * @author alben.wong
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ValidatorChainTest {

    @Autowired
    private SpringValidatorFactory springValidatorFactory;
    @Autowired
    private StockVoValidatorV2 stockVoValidatorV2;
    @Autowired
    private StockSceneProvider stockSceneProvider;

    @Test
    public void test1(){
        StockVo stockVo = new StockVo();
        stockVo.setId(1L);
        stockVo.setVendorCode("123");
        stockVo.setType(1);
        try{
            ValidatorChain.build()
                    .failFast(false)
                    .target(stockVo)
                    .sceneProvider(stockSceneProvider)
                    .useFactory(springValidatorFactory)
                    .add(StockVoValidator.class, Arrays.asList("scene1"))
                    .add(stockVoValidatorV2, Arrays.asList("scene2"))
                    .setAttribute("attribute1", "123123")
                    .callback(new ValidatorCallback() {
                        @Override
                        public void onSuccess(Object target, List list, ValidatorContext context) {
                            log.info("onSuccess");
                        }

                        @Override
                        public void onFail(Object target, List list, ValidatorContext context) {
                            log.info("onFail");
                        }

                        @Override
                        public void onException(Object target, List list, ValidatorContext context, Exception e) {
                            log.info("onUncaughtException");
                        }
                    })
                    .doValidate()
                    .result((s, strings) -> {
                        System.out.println(s);
                        System.out.println(strings);
                    });
        }catch (Exception e){
            log.error("11", e);
        }
    }
}
