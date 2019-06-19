package com.albenw;

import com.albenw.validatorchain.base.ValidatorAdapter;
import com.albenw.validatorchain.base.ValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author alben.wong
 */
@Slf4j
@Component
public class StockVoValidator extends ValidatorAdapter<StockVo> {

    @Override
    public boolean accept(StockVo target, ValidatorContext context) throws Exception {
        log.info("StockVoValidator into accept");
        return true;
    }

    @Override
    public boolean validate(StockVo target, ValidatorContext context) throws Exception {
        log.info("StockVoValidator into validate");
        String attribute1 = context.getAttribute("attribute1", String.class);
        log.info("attribute1={}", attribute1);
        if(target.getId() == null){
            context.appendError("stock", "合作编码为空");
            return false;
        }
        return true;
    }

    @Override
    public void onException(StockVo target, ValidatorContext context, Exception e) throws Exception {
        log.error("StockVoValidator into onException");
    }

}
