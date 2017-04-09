$(function(){
    //绑定单选钮事件
    $(document).on('click','.radioLabel',function(e){
        e.preventDefault();
        e.stopPropagation();
        var radioName = $(this).attr('radioName');
        var id =$(this).attr('id');
        $('.radioLabel[radioName="'+radioName+'"]').removeClass('checked');
        $(this).addClass('checked');
        $('input[for="'+id+'"]').prop('checked',true);
        //alert('选中value='+$('input[name="'+radioName+'"]:checked').val()+'的单选钮')
    });

    //绑定单选钮事件
    $(document).on('click','.checkboxLabel',function(e){
        e.preventDefault();
        e.stopPropagation();
        var checkboxName = $(this).attr('checkboxName');
        var id =$(this).attr('id');
        var $this = $(this);
        $this.toggleClass('checked');
        $('input[for="'+id+'"]').prop('checked',$this.hasClass('checked'));
        //alert('选中value='+$('input[name="'+radioName+'"]:checked').val()+'的复选钮')
    });
})
