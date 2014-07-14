/**
 * @author rosten
 */
define(["dojo/dom",
        "dijit/registry",
        "dojo/date/locale",
        "rosten/kernel/behavior"], function(dom,registry,dateLocale) {
    
    question_add = function(){
        var questionTitle = registry.byId("questionTitle");
        if(!questionTitle.isValid()){
            rosten.alert("标题不正确！").queryDlgClose = function(){
                questionTitle.focus();
            };
            return;
        }
        
        var question = registry.byId("question");
        if(question.attr("value")==""){
            rosten.alert("问题描述不正确！").queryDlgClose = function(){
                question.focus();
            };
            return;
        };

        var questionTime = registry.byId("questionTime");
        if(!questionTime.isValid()){
            rosten.alert("提问时间不正确！").queryDlgClose = function(){
                questionTime.focus();
            };
            return;
        }
                
        var content = {};
        var unid = registry.byId("unid");
        if(unid.attr("value")!=""){
            content.unid = unid.attr("value");
        }
        content.questionTitle = questionTitle.attr("value");
        content.question = question.attr("value");
        
        var time = questionTime.attr("value");
        var timeStr = dateLocale.format(time, {selector:'datetime', datePattern:'yyyy-MM-dd',timePattern:'HH:mm:ss'});
        content.questionTime = timeStr;

        var isAnswer = registry.byId("isAnswer");
        if(isAnswer.attr("value")!=""){
            content.isAnswer = isAnswer.attr("value");
        }
        var questionAnswer = registry.byId("questionAnswer");
        if(questionAnswer.attr("value")!=""){
            content.questionAnswer = questionAnswer.attr("value");
        }
        
        rosten.readSync(rosten.webPath + "/system/questionSave",content,function(data){
            if(data.result=="true"){
                rosten.alert("保存成功！").queryDlgClose= function(){
                    page_quit();    
                };
            }else{
                rosten.alert("保存失败!");
            }
        });
    };
    addQuestion = function(){
        var questionTitle = registry.byId("questionTitle");
        if(!questionTitle.isValid()){
            rosten.alert("标题不正确！").queryDlgClose = function(){
                questionTitle.focus();
            };
            return;
        }
        
        var question = registry.byId("question");
        if(question.attr("value")==""){
            rosten.alert("问题描述不正确！").queryDlgClose = function(){
                question.focus();
            };
            return;
        }

        var questionTime = registry.byId("questionTime");
        if(!questionTime.isValid()){
            rosten.alert("提问时间不正确！").queryDlgClose = function(){
                questionTime.focus();
            };
            return;
        }
                
        var content = {};
        var unid = registry.byId("unid");
        if(unid.attr("value")!=""){
            content.unid = unid.attr("value");
        }
        content.questionTitle = questionTitle.attr("value");
        content.question = question.attr("value");
        content.username = rosten.kernel.getUserInforByKey("username");
        
        var time = questionTime.attr("value");
        var timeStr = dateLocale.format(time, {selector:'datetime', datePattern:'yyyy-MM-dd',timePattern:'HH:mm:ss'});
        content.questionTime = timeStr;

        rosten.readSync(rosten.webPath + "/system/questionSave",content,function(data){
            if(data.result=="true"){
                rosten.kernel.hideRostenShowDialog();
                rosten.alert("感谢您所提的问题,我们将尽快给予解决，谢谢！");
            }else{
                rosten.alert("保存失败!");
            }
        });
    };

});
