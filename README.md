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


