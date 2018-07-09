network-interaction-master
==========================================
集成Rxjava+Retrofit+Okhttp网络链式请求。

使用方式：
<br/>
     1、创建Application类  Utils初始化{ Utils.init(this);}
<br/>
     2、修改library的Constants类的请求地址
<br/>
     3、创建Service类  定义网络请求接口
<br/>
     4、创建返回数据实体类
<br/>
     5、调用方式<br/>
```java
     RetrofitHelper.getApiService(Constants.GANK_HTTP_ADDRESS)
                .getWelfare(20, currentPages)
                .compose(this.<List<Welfare>>bindToLifecycle())
                .compose(ProgressUtils.<List<Welfare>>applyProgressBar(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<Welfare>>() {
                    @Override
                    public void onSuccess(List<Welfare> response) {
                        // TODO: 2018/7/9 成功
                        LogUtils.e("TAG", "请求成功：" + response.toString());
                    }

                    @Override
                    public void onFinish() {
                        // TODO: 2018/7/9 失败
                        LogUtils.e("TAG", "请求失败");
                    }
                });
```
     
