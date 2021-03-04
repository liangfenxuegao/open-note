/******************************************
 * My Login
 *
 * Bootstrap 4 Login Page
 *
 * @author          Muhamad Nauval Azhar
 * @uri 			https://nauval.in
 * @copyright       Copyright (c) 2018 Muhamad Nauval Azhar
 * @license         My Login is licensed under the MIT license.
 * @github          https://github.com/nauvalazhar/my-login
 * @version         1.2.0
 *
 * Help me to keep this project alive
 * https://www.buymeacoffee.com/mhdnauvalazhar
 * 
 ******************************************/

'use strict';//使用严格模式

$(function() {
	//选择input标签中type为password，且有data-eye属性的标签，遍历其值。本函数的是用于实现查看密码功能的。
	$("input[type='password'][data-eye]").each(function(i) {
		const $this = $(this), id = 'eye-password-' + i, el = $('#' + id);//const定义的是常量

		$this.wrap($("<div/>", {
			style: 'position:relative',
			id: id
		}));

		$this.css({
			paddingRight: 60
		});
		$this.after($("<div/>", {
			html: '显示',//调整密码框中的提示文本
			class: 'btn btn-primary btn-sm',
			id: 'passeye-toggle-'+i,
		}).css({
				position: 'absolute',
				right: 10,
				top: ($this.outerHeight() / 2) - 12,
				padding: '2px 7px',
				fontSize: 12,
				cursor: 'pointer',
		}));

		$this.after($("<input/>", {
			type: 'hidden',
			id: 'passeye-' + i
		}));

		const invalid_feedback = $this.parent().parent().find('.invalid-feedback');

		if(invalid_feedback.length) {
			$this.after(invalid_feedback.clone());
		}

		$this.on("keyup paste", function() {
			$("#passeye-"+i).val($(this).val());
		});
		$("#passeye-toggle-"+i).on("click", function() {
			if($this.hasClass("show")) {
				$this.attr('type', 'password');
				$this.removeClass("show");
				$(this).removeClass("btn-outline-primary");
			}else{
				$this.attr('type', 'text');
				$this.val($("#passeye-"+i).val());
				$this.addClass("show");
				$(this).addClass("btn-outline-primary");
			}
		});
	});

	//当提交类型为my-login-validation的表单时，触发本段程序
	// $(".my-login-validation").submit(function() {
	// 	//取得表单
	// 	const form = $(this);
	// 	//当form[0]的验证结果为false时，执行如下事件
	// 	if (form[0].checkValidity() === false) {
    //       event.preventDefault();//阻止点击提交按钮时对表单的提交
    //       event.stopPropagation();//阻止事件冒泡到父元素，阻止任何父事件处理程序被执行。
    //     }
	// 	//为该表单添加类，was-validated意为“已验证”。
	// 	form.addClass('was-validated');
	// });
});
