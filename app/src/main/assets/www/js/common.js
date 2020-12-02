/* Jar 2020 by wwl */
var common = {
  initialize: function(){
    w3.addClass('[w3-repeat]','hide');
    w3.hide('.toast');
  },
  backToEdit: function(ipt){
      if(ipt&&ipt.tagName=="INPUT"){
          ipt.select();
      }
  },
  backToClear: function(ipt){
      if(ipt&&ipt.tagName=="INPUT"){
          ipt.value="";
          ipt.focus();
      }
  },
  setNextFocus: function(el){
      try{
          let next = el.parentNode.nextElementSibling;
          if(next){
              next = next.nextElementSibling;
          }else{
              next = el.parentNode.parentNode.querySelector('.w3-rest');
          }
          next = next.firstElementChild;
          if(next.tagName=="INPUT"||next.tagName=="SELECT"||next.tagName=="TEXTAREA"){
              next.focus()
          }
      }catch(e){
          console.log(e);
      }
  },
  displayPost: function(id,target,type,data){
    this.post(target,type,data, function () {
        if (this.readyState == 4 && this.status == 200) {
            w3.displayObject(id, JSON.parse(this.responseText));
        }
    });
  },
  /**
   * 
   * @param {string} target url
   * @param {enum} type formdata,json
   * @param {json} data
   * @param {function} readyfunc 
   */
  post: function(target,type,data,readyfunc){
    var httpObj,httpData;
    if (window.XMLHttpRequest) {
        httpObj = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        httpObj = new ActiveXObject("Microsoft.XMLHTTP");
    }
    if(!(typeof(data) == "object" && Object.prototype.toString.call(data).toLowerCase() == "[object object]" && !data.length)){
        alert('data不是json对象');
        return;
    }
    switch(type){
        case 'formdata':
            httpData=new FormData();
            for(var key in data){
                httpData.append(key,data[key]);
            }
            break;
        case 'json':
            xhr.setRequestHeader("Content-Type", "application/json");
            httpData = JSON.stringify(data);
            break;
        default: 
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            httpData = "";
            for(var key in data){
                httpData = httpData + key +'='+data[key] + '&';
            }
            httpData = httpData.substring(0,httpData.length-1);
            break;
    }
    if (httpObj) {
        if (readyfunc) {httpObj.onreadystatechange = readyfunc;}
        httpObj.open("post", target, true);
        httpObj.send(httpData);
    }
  },
  // 模拟tab键（无效）：this.fireKeyEvent(e.target,'keydown',9);
  fireKeyEvent: function(el, evtType, keyCode){
      var doc = el.ownerDocument,
          win = doc.defaultView || doc.parentWindow,
          evtObj;
      if(doc.createEvent){
          if(win.KeyEvent) {
              evtObj = doc.createEvent('KeyEvents');
              evtObj.initKeyEvent( evtType, true, true, win, false, false, false, false, keyCode, 0 );
          }
          else {
              evtObj = doc.createEvent('UIEvents');
              Object.defineProperty(evtObj, 'keyCode', {
                  get : function() { return this.keyCodeVal; }
              });     
              Object.defineProperty(evtObj, 'which', {
                  get : function() { return this.keyCodeVal; }
              });
              evtObj.initUIEvent( evtType, true, true, win, 1 );
              evtObj.keyCodeVal = keyCode;
          }
          el.dispatchEvent(evtObj);
      } 
      else if(doc.createEventObject){
          evtObj = doc.createEventObject();
          evtObj.keyCode = keyCode;
          el.fireEvent('on' + evtType, evtObj);
      }
  }
}