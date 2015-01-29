$.ajaxSetup ({
    cache: false
});


$(function() {
    reloadNetworks();
    
    /**
     * Handlers for the setup tab
     */
    $("#reloadNetworks").click(function() {
        reloadNetworks();
    });
    
    $("#saveButton").click(function() {
        loading("show");
        var passwd = $("#passwd").val();
        var ssid = $("#network").val();
        var post = $.post( "/hotspot/save", {'password':passwd, 'ssid':ssid});
        post.done(function( data ) {
            //nothing
        });
        loading("hide");
    });
    
    $("#shutdownButton").click(function() {
        loading("show");
        $.getJSON('/hotspot/shutdown', function(data) {
            //nothing
        });
        loading("hide");
    });

    
    /**
     * Handlers for the console tab
     */
     $("#commandSubmit").click(function() {
        loading("show");
        var command = $("#commands").val();
        var post = $.post( "/hotspot/command", {'command':command});
        post.done(function( data ) {
            $("#output").val(data.output);
            $("#erroutput").val(data.error);
        });
        loading("hide");
     });
     
    $("#commandClear").click(function() {
        $("#commands").val('');
        $("#output").val('');
        $("#erroutput").val('');
    });
});

function reloadNetworks() {
    loading("show");
    $.getJSON('/hotspot/networks', function(data) {
        $('#network').empty();
        $.each(data, function(key, value) {
            for(var i=0; i<value.length; i++) {
                var val = value[i];
                var option = $('<option />');
                option.attr('value', val.ssid).text(val.ssid + ' (' + val.authentication + ')');
                if(i == 0) {
                    option.attr('selected', 'true');
                }
                $('#network').append(option);
            }
        });
        $('#network').selectmenu('refresh');
    });
    loading("hide");
}

/**
 *
 */
function loading(showOrHide) {
    setTimeout(function(){
        $.mobile.loading(showOrHide);
    }, 1); 
}