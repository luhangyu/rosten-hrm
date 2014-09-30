/**
 * @author rosten
 */
define(["dojo/dom",
        "dijit/registry",
        "rosten/widget/PickTreeDialog",
        "rosten/app/Application",
        "rosten/kernel/behavior"], function(dom,registry,PickTreeDialog) {
	
	staff_addFamily = function(){
		
	};
    page_quit_1 = function(flag) {
        var parentNode = window.opener;
        window.close();
        if (flag && flag == true) {
            parentNode.refreshSystem();
        }
    };
    page_quit = function() {
        rosten.pagequit();
    };
    
});
