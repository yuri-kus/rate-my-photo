var RateMyPhoto = {};

RateMyPhoto.initPhotoBrowser = function() {
    $("#file-tree").fancytree({
        source: {
            url: "dirs"
        },
        lazyLoad: function(event, data) {
            var node = data.node;
            data.result = {
                url: "dirs",
                data: {
                    rootDir: encodeURIComponent(node.key)
                }
            }
        },
        click: function(event, data) {
            console.log(data.node.key);
            $.ajax({
                url: "photoList",
                data: { rootDir: data.node.key },
                success: function(data){
                    showPhotos(data);
                }
            });

            // return false to prevent default behavior (i.e. activation, ...)
            return true;
        }
    });

    var showPhotos = function(data) {
        console.log(data);

        $("#photo-gallery-container").empty();

        var $newDiv = $("<div/>").addClass("photo-gallery")
        $("#photo-gallery-container").append($newDiv);

        $.each(data, function( index, value ) {
            var $img = $("<img/>")
                .attr("src", "thumbnail?photoId=" + value.id)
                .html("<figure>value.fileInfo.name</figure>");
            var $a = $("<a/>")
                .attr("href", "photo?photoId=" + value.id)
                .attr("data-size", value.imageInfo.fullWidth + "x" + value.imageInfo.fullHeight)
                .attr("data-med", "photo?photoId=" + value.id)
                .attr("data-med-size", value.imageInfo.fullWidth + "x" + value.imageInfo.fullHeight)
                .attr("data-author", "yurikus")
                .addClass("demo-gallery__img--main");
            $a.append($img);
            $newDiv.append($a)
        });

        console.log("before init photoswipe");
        initPhotoSwipeFromDOM('.photo-gallery');
    }
}

// preventing errors from firebug console calls left in production code
if (!console) {
    var console = {
        log : function() {
        },
        debug : function() {
        }
    };
}