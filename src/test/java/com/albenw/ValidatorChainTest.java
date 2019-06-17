package com.albenw;

import com.albenw.validatorchain.*;
import com.albenw.validatorchain.base.ValidatorContext;
import com.albenw.validatorchain.base.ValidatorUnit;
import com.albenw.validatorchain.factory.SpringValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author alben.wong
 */
@Slf4j
public class ValidatorChainTest {

    @Autowired
    private SpringValidatorFactory springValidatorFactory;
    @Autowired
    private StockVoValidatorV2 stockVoValidatorV2;

    @Test
    public void test1(){
        StockVo stockVo = new StockVo();
        stockVo.setPaId(null);
        stockVo.setVendorCode("");
        try{
            ValidatorChain.build()
                    .failFast(false)
                    .useFactory(springValidatorFactory)
                    .addLast(stockVo, StockVoValidator.class)
                    .addLast(stockVo, stockVoValidatorV2)
                    .setAttribute("attribute1", "123123")
                    .callback(new ValidatorCallback() {
                        @Override
                        public void onSuccess(List<ValidatorUnit> units, ValidatorContext context) {
                            log.info("callback on success");
                        }

                        @Override
                        public void onFail(List<ValidatorUnit> units, ValidatorContext context) {
                            log.info("callback on fail");
                        }

                        @Override
                        public void onUncaughtException(List<ValidatorUnit> units, ValidatorContext context, Exception e) {
                            log.error("callback on exception");
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
