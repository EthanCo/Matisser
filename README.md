在[Matisse 0.5.2-beata4 (2019年5月)](https://github.com/zhihu/Matisse)的基础上，封装了权限申请、图片压缩及图片裁剪，并支持扩展
	
- 升级新版本方便
	- 所有的扩展仅在扩展包中实现，对Matisse原有代码解构不产生影响，仅把几个修饰符修改成了public，后期如果Matisse更新了新版本，可很方便地进行替换新版本
- 集成了权限申请，使用简单
- 支持扩展，比如图片压缩、图片裁剪等，一个类搞定  

### 如何使用

1.将`Matisse.from(context)`替换为`Matisser.from(context)`

	Matisser.from(MatisserSampleActivity.this)
        .choose(MimeType.ofImage())
        .theme(R.style.Matisse_Dracula)
        .countable(false)
        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
        .maxSelectable(9)
        .originalEnable(true)
        .maxOriginalSize(10)
        .imageEngine(new PicassoEngine())
        .forResult(REQUEST_CODE_CHOOSE);

2.实现onActivityResult

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Matisser.onActivityResult(this, requestCode, resultCode, data)) return;

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> paths = Matisser.obtainPathResult(data);
			
            Matisser.handleResult(this,"sample", paths, new Matisser.HandleResult() {
                @Override
                public void onResult(List<String> urls) {
					//最终操作后的回调
                    //do something
                }
            });
        }
    }

### 进行扩展
以下以Luban图片压缩为例  
新建LubanTransactor，继承自Transactor  

	public class LubanTransactor extends Transactor {
	
	    @Override
	    public void handle(final Matter matter, final Activity activity) {
	        final File file = new File(matter.getRequest());
	        Log.i("OnActivityResult ", "file.size:" + file.length());
	        Luban.with(activity)
	                .load(file)
	                .ignoreBy(100)
	                .setTargetDir(activity.getExternalCacheDir().toString())
	                .filter(new CompressionPredicate() {
	                    @Override
	                    public boolean apply(String path) {
	                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
	                    }
	                })
	                .setCompressListener(new OnCompressListener() {
	                    @Override
	                    public void onStart() {
	                        // 压缩开始前调用，可以在方法内启动 loading UI
	                    }
	
	                    @Override
	                    public void onSuccess(File file) {
	                        // 压缩成功后调用，返回压缩后的图片文件
	                        Log.i("OnActivityResult", "Luban压缩成功:" + file.toString() + " file.size:" + file.length());
	                        matter.setRequest(file.getPath());
	                        getNext().handle(matter, activity);
	                    }
	
	                    @Override
	                    public void onError(Throwable e) {
	                        // 当压缩过程出现问题时调用
	                        Log.e("OnActivityResult", "Luban压缩失败:" + e.getMessage());
	                        getNext().handle(matter, activity);
	                    }
	                }).launch();
	    }
	}

在Application中添加这个Transactor  

	Matisser.addTransactor(new LubanTransactor());  

然后，在`Matisser.handleResult`的回调中，取到的就是压缩后的图片的URL了