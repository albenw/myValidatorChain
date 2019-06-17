package com.albenw;

import com.albenw.validatorchain.base.ValidatorAdapter;
import com.albenw.validatorchain.base.ValidatorContext;
import com.albenw.validatorchain.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author alben.wong
 */
@Slf4j
@Component
public class StockVoValidatorV2 extends ValidatorAdapter<StockVo> {

    @Override
    public boolean accept(StockVo target, ValidatorContext context) throws Exception {
        log.info("StockVoValidatorV2 into accept");
        return true;
    }

    @Override
    public boolean validate(StockVo target, ValidatorContext context) throws Exception {
        log.info("StockVoValidatorV2 into validate");
        if(StringUtils.isBlank(target.getVendorCode())){
            context.appendError("stock", "供应商为空");
            return false;
        }
        return true;
    }

    @Override
    public void onException(StockVo target, ValidatorContext context, Exception e) throws Exception {
        log.error("StockVoValidatorV2 into onException");
    }
}
