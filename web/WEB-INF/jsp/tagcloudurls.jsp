<style type="text/css">
* {
  margin:  0;
  padding: 0;
}
div#mytagcloud {
  width: 450px;
}
ul.tagcloud-list {
  border: 1px solid #000;
  font-size: 100%;
  font-weight: bold;
  font-family: "Arial", "sans-self";
  padding: 2px;
  margin: 10px;
}
li.tagcloud-base {
  font-size: 24px;
  display: inline;
}
a.tagcloud-anchor {
  text-decoration: none;
}
a.tagcloud-ealiest {
  color: #ccc;
}
a.tagcloud-earlier {
  color: #99c;
}
a.tagcloud-later {
  color: #99f;
}
a.tagcloud-latest {
  color: #00f;
}
</style>
<script type="text/javascript" src="js/lyokato-javascript-tagcloud-05ffc75/lib/tagcloud.js"></script>
<script type="text/javascript" src="js/urlstagcloud.js"></script>
<!--h1 onclick="setuptc(\"${urlsOccurZoomOne}\");">Tag cloud of urls</h1-->
Tag cloud of urls
<script type="text/javascript">
    //setup("${urlsOccur}");//creates the tag cloud
    var urlsOccur = "${urlsOccurZoomOne}";//"${urlsOccurView}";
    //alert(urlsOccur);
    setuptc(urlsOccur, 'tagcloud');
</script>
<div id="mytagcloud"></div>   