/**
 *   js的功能：1.从后台获取到店铺分类以及区域分类等信息并填充到前台的控件里面去
 *            2.将表单的信息获取到，然后将它转发到后天去注册店铺
 */
$(function () {
    // 用于店铺注册时候的店铺类别以及区域列表的初始化的URL
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    // 编辑店铺信息的URL
    var registerShopUrl = '/o2o/shopadmin/registershop';

    alert(initUrl);
    //js文件一加载的时候就调用getShopInitInfo对店铺信息进行初始化
    getShopInitInfo();

    // 通过店铺Id获取店铺信息
    function getShopInitInfo() {
        /**
         * 通过ajax对数据进行获取封装并填充至html中
         * $.getJSON()是js对ajax的封装,参数一是要访问的url，参数二就是回调的方法
         * $.getJSON()和$.ajax的功能是一样的，它们的区别：
         * $.getJSON()是专门为ajax获取json数据而设置的，并且支持"跨域"调用
         * $.ajax是jquery对ajax的封装。返回的是Json字符串，而$.getJSON返回的是JSON对象
         */
        $.getJSON(initUrl,function (data) {
            //后台数据返回的success
            if(data.success){
                var tempHtml='';
                var tempAreaHtml='';
                /**
                 * 这里是从后台返回的查询店铺类别以及区域信息。通过data.属性名的方式获取相应的list，
                 * 然后调用map()方法进行遍历。item是遍历中的具体元素，遍历的过程会进行html的拼接，
                 * 最终追加在页面上。map中只有一个参数，即function函数，在函数中将店铺分类或区域信息
                 * 组成option标签，赋值给tempHtml，最终追加到页面中
                 */
                //获取到对应的店铺分类响应数据
                data.shopCategoryList.map(function (item,index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">'
                        + item.shopCategoryName +'</option>';
                });
                //获取到对那个的店铺区域信息响应数据
                data.areaList.map(function (item,index) {
                   tempAreaHtml += '<option data-id="' + item.areaId + '">'
                    + item.areaName + '</option>';
                });
                //将数据填充至html中
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
    }

    //提交按钮的事件响应，分别对店铺注册和编辑操作做不同响应
    $('#submit').click(function () {
        var shop = {};
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        // 选择选定好的店铺类别
        shop.shopCategory = {
            /**
             * $('#shop-category').find('option')是从id为shop-category的元素中，寻找option元素。
             * not是一个筛选器，筛选当前集合以获取与CSS选择器不匹配的元素的新集合。
             * this.selected是被选中的集合，!this.selected则是没有被选中的集合。
             * 双重否定表肯定。其实本质上就相当于是得到了#shop-category选中的option。data('id')是获取 option 的 data-id 属性值。
             */
            shopCategoryId : $('#shop-category').find('option').not(function() {
                return !this.selected;
            }).data('id')
        };
        // 选择选定好的区域信息
        shop.area = {
            areaId : $('#area').find('option').not(function() {
                return !this.selected;
            }).data('id')
        };
        // 获取上传的图片文件流:
        // $('#shop-img')返回的是一个列表，由于只有一个id为shop-img的元素，所以$('#shop-img')[0]就是获取具体元素
        // 而$('#shop-img')[0]中的文件也是一个列表，由于只有一个文件，所以$('#shop-img')[0].files[0]就是获取就具体的文件数据
        var shopImg = $('#shop-img')[0].files[0];
        // 生成表单对象，用于接收参数并传递给后台
        var formData = new FormData();
        // 添加图片流进表单对象里
        formData.append('shopImg', shopImg);
        // 将shop json对象转成字符流保存至表单对象key为shopStr的的键值对里
        formData.append('shopStr', JSON.stringify(shop));
        // 获取表单里输入的验证码
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        formData.append('verifyCodeActual', verifyCodeActual);
        // 将数据提交至后台处理相关操作
        $.ajax({
            url :  registerShopUrl,
            type : 'POST',
            data : formData,
            contentType : false,
            processData : false,
            cache : false,
            success : function(data) {
                if (data.success) {
                    $.toast('提交成功！');
                } else {
                    $.toast('提交失败！' + data.errMsg);
                }
            }
        });
    });
})