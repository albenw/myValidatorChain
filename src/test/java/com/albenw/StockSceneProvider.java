package com.albenw;

import com.albenw.validatorchain.base.SceneProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alben.wong
 * @since 2019-06-19.
 */
@Component
public class StockSceneProvider implements SceneProvider<StockVo> {

    @Override
    public List<String> decide(StockVo target) {
        List<String> scenes = new ArrayList<>();
        if(target.getType() == 1){
            scenes.add("scene1");
        }
        if(target.getType() == 2){
            scenes.add("scene2");
        }
        return scenes;
    }
}
