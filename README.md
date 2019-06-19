### 概要

这是一个简单的场景校验器，采用类似责任链的模式。旨在把一些复杂的业务场景校验逻辑分解，增加代码的可读性，可维护性，可扩展性。

### 背景

有多的业务场景往往是这样的，在一个统一的入口，对一个参数（业务对象）进行一大堆业务逻辑校验后，才正式做真正的业务行为，如通知上游系统，写DB、缓存，这些业务往往是重业务，例如订单的创建，里面的逻辑会相当复杂，涉及到很多业务场景，业务模式等。刚开始时可能业务比较简单，代码量少，但是随着业务场景、模式的增加，开发人员往往会不停的在上面"追加"代码，导致代码越来越"难堪"，一大坨的代码增加维护成本和对新加入开发人员的熟悉成本。

### 使用

#### SceneProvider

一个定义"场景"的接口。"场景"内部用字符串String来表示，decide方法返回List\<String>表示在某些条件下，这个业务对象是属于那几个业务场景（模式）的。

例子：

```java
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
```



#### Validator

定义一个校验器，至于这个校验器需要做什么，由你定义。这个校验器可以适用于某些业务场景又或是通用的。

例子：

```
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
```



ValidatorAdapter是对Validator的一个适配，提供了默认的继承方法。

1. accept方法表示是否校验是否适用
2. validate方法，校验逻辑的地，返回值boolean表示成功或者失败，对于校验失败的错误信息可以通过context.appendError方法后在结果集取出来。
3. onException方法会在validate方法抛出异常时回调



#### 完整的例子

```java
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
```









