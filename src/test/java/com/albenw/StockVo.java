package com.albenw;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author alben.wong
 */
@Setter
@Getter
public class StockVo {

    private Long paId;
    private String vendorCode;
    private Integer type;
    private List<StockItemVo> items;

    @Setter @Getter
    class StockItemVo{
        private String barcode;
        private String po;
        private List<WarehouseItem> warehouseItems;
    }

    @Getter @Setter
    class WarehouseItem{
        private String warehouse;
        private int count;
    }
}
