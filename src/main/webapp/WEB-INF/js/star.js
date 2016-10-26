/**
 * Created by user on 2016/10/26.
 */
function genStar(total,count){
    var starHtml = '<fieldset class="starability-checkmark" >';
    var currentIndex = total;
    for(var i = total;i >= 1;i--){
        if(i <= count){
            starHtml += '<label style="background-position: 0 -30px;">'+currentIndex+' stars</label>';
        }else{
            starHtml += '<label>'+currentIndex+' stars</label>';
        }
        currentIndex --;
    }
    starHtml +='</fieldset>';
    return starHtml;
}