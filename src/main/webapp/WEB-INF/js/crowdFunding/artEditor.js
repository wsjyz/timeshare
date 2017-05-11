/**
 * 移动端富文本编辑器
 * @author ganzw@gmail.com
 * @url    https://github.com/baixuexiyang/artEditor
 */
$.extend($.fn,{
    _opt: {
        placeholader: '<p>请输入文章正文内容</p>',
        validHtml: [],
        limitSize: 3,
        showServer: false,
        isReturnCutPath:true
    },
    artEditor: function (options) {
        var _this = this,
            styles = {
                "-webkit-user-select": "text",
                "user-select": "text",
                "overflow-y": "auto",
                "text-break": "brak-all",
                "outline": "none",
                "cursor": "text"
            };
        $(this).css(styles).attr("contenteditable", true);
        _this._opt = $.extend(_this._opt, options);
        try {
            $(_this._opt.imgTar).on('change', function (e) {
                var file = e.target.files[0];
                e.target.value = '';
                if (Math.ceil(file.size / 1024 / 1024) > _this._opt.limitSize) {
                    console.error('上传的文件太大,限制为：'+_this._opt.limitSize+"MB以内");
                    return;
                }
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function (f) {
                    var data = f.target.result,
                        img = new Image();
                    img.src = f.target.result;
                    if(_this._opt.compressSize && Math.ceil(file.size / 1024 / 1024) > _this._opt.compressSize) {
                        data = _this.compressHandler(img);
                    }
                    if (_this._opt.showServer) {
                        //_this.upload(data);
                        _this.upload(file,_this._opt.isReturnCutPath);
                        return;
                    }
                };
            });
            _this.placeholderHandler();
            _this.pasteHandler();
        } catch (e) {
            console.log(e);
        }
        if (_this._opt.formInputId && $('#' + _this._opt.formInputId)[0]) {
            $(_this).on('input', function () {
                $('#' + _this._opt.formInputId).val(_this.getValue());
            });
        }
    },
    compressHandler: function(img) {
        var canvas = document.createElement("canvas");
        var ctx = canvas.getContext('2d');
        var tCanvas = document.createElement("canvas");
        var tctx = tCanvas.getContext("2d");
        var initSize = img.src.length;
        var width = img.width;
        var height = img.height;
        var ratio;
        if ((ratio = width * height / 4000000) > 1) {
            ratio = Math.sqrt(ratio);
            width /= ratio;
            height /= ratio;
        } else {
            ratio = 1;
        }
        canvas.width = width;
        canvas.height = height;
        ctx.fillStyle = "#fff";
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        var count;
        if ((count = width * height / 1000000) > 1) {
            count = ~~(Math.sqrt(count) + 1);
            var nw = ~~(width / count);
            var nh = ~~(height / count);
            tCanvas.width = nw;
            tCanvas.height = nh;
            for (var i = 0; i < count; i++) {
                for (var j = 0; j < count; j++) {
                    tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh);
                    ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh);
                }
            }
        } else {
            ctx.drawImage(img, 0, 0, width, height);
        }
        var ndata = canvas.toDataURL('image/jpeg', 0.1);
        tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0;
        return ndata;
    },
    upload: function (file,isReturnCutPath) {
        var _this = this, filed = _this._opt.uploadField;
        if ($.trim($(_this).html()) === _this._opt.placeholader) {
            $(_this).html('');
        }

        var form = new FormData();
        form.append("inputFile", file);
        form.append("isReturnCutPath", isReturnCutPath);
        $.ajax({
            url: _this._opt.uploadUrl,
            type: 'post',
            data: form,
            contentType: false,
            processData: false,
            cache: false,
            async: false,
        }).then(function (res) {
                var src = _this._opt.uploadSuccess(res);
                if (src) {
                    var img = '<img src="' + src + '" style="height: calc(525/900*100vw);width: 100%;" />';
                    _this.insertImage(img);
                } else {
                    console.log('图片地址为空', src)
                }
            }, function (error) {
                _this._opt.uploadError(error.status,error);
            })
    },
    insertImage: function (src) {
        $(this).focus();
        var selection = window.getSelection ? window.getSelection() : document.selection;
        var range = selection.createRange ? selection.createRange() : selection.getRangeAt(0);
        if (!window.getSelection) {
            range.pasteHTML(src);
            range.collapse(false);
            range.select();
        } else {
            range.collapse(false);
            var hasR = range.createContextualFragment(src);
            var hasLastChild = hasR.lastChild;
            while (hasLastChild && hasLastChild.nodeName.toLowerCase() == "br" && hasLastChild.previousSibling && hasLastChild.previousSibling.nodeName.toLowerCase() == "br") {
                var e = hasLastChild;
                hasLastChild = hasLastChild.previousSibling;
                hasR.removeChild(e);
            }
            range.insertNode(range.createContextualFragment("<br/>"));
            range.insertNode(hasR);
            if (hasLastChild) {
                range.setEndAfter(hasLastChild);
                range.setStartAfter(hasLastChild);
            }
            selection.removeAllRanges();
            selection.addRange(range);
        }
        if (this._opt.formInputId && $('#' + this._opt.formInputId)[0]) {
            $('#' + this._opt.formInputId).val(this.getValue());
        }
    },
    pasteHandler: function () {
        $(this).on("paste", function (e) {
        });
    },
    placeholderHandler: function () {
        var _this = this;
        $(this).on('focus', function () {
            if ($.trim($(this).html()) === _this._opt.placeholader) {
                $(this).html('');
            }
        })
            .on('blur', function () {
                if (!$(this).html()) {
                    //$(this).html(_this._opt.placeholader);
                }
            });

        if (!$.trim($(this).html())) {
            $(this).html(_this._opt.placeholader);
        }
    },
    getValue: function () {
        //当没有插入图片以及没有任何文字输入（只有css样式）时，将编辑内容区域清空
        if(!$(this).text() && $(this).html().indexOf('<img src="/time/images')==-1){
            $(this).html('');
            $(this).focus();
        }
        return $(this).html();
    },
    setValue: function (str) {
        $(this).html(str);
    }
});
