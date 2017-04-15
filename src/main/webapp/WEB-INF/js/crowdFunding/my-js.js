$(function() {
"use strict";
 //操作表
  $(document).on("pageInit", ".page", function(e, id, page) {
    $(page).on('click','.create-actions', function () {
      var buttons1 = [			
			{
			  text: '强制下架',			 
			  onClick: function() {
			  }
			},
			{
			  text: '讨论管理',
			  onClick: function() {
			  }
			}			
		  ];
		  var buttons2 = [
			{
			  text: '取消',
			  bg: 'success'
			}
		  ];
		  var groups = [buttons1, buttons2];
		  $.actions(groups);
    });
  });

  // 默认必须要执行$.init()
  $.init();		
});

