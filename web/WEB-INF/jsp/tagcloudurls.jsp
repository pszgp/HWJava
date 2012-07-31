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
<script type="text/javascript">
<!--

var tc = TagCloud.create();

//function setup(urlsOccur) {
  
  //alert(urlsOccur);
  
    //pszgp: use an existing tagcloud function
    //  to add the urls to the tag, then call the existing tagcloud function 
    
    var urlsOccur = "${urlsOccur}";
    //alert(urlsOccur);
    if (urlsOccur!=null)
    {
        var data = [];
        if (urlsOccur.indexOf("{")==0)
        {
            urlsOccur = urlsOccur.substring(1, urlsOccur.length-1); 
            data = urlsOccur.split(", ");
        }
        for (var i=0;i<data.length;i++)
        {
            //alert(data[i]);
            var url = {};
            var name = data[i];
            var occur = data[i];
            
            name = name.substring(0, name.indexOf("="));
            
            occur = occur.substring(occur.indexOf("=")+1, occur.length);
            //alert(data[i]);//+" AND " +occur);
            occur = +occur;
            if (occur > 1000)
                occur/=10;
            else if ((occur < 50) && (occur > 10))
                occur *= 5;
            else if (occur < 10)
                occur *= 50;
            
            url.name = name;
            url.occur = occur;
            
            //alert(url.name+" "+url.occur);
            
            tc.add(url.name, url.occur, url.name, Date.parse('005/06/23 00:00:00'));
            
        }
     }
    
//}

tc.loadEffector('CountSize').base(10).range(5);
tc.loadEffector('DateTimeColor');

    //tc.setup('mytagcloud');
//}

function setup ()
{
    tc.setup('mytagcloud');    
}
//-->
</script>

<h1 onclick="setup();">Tag cloud of urls</h1>

</body>
<script type="text/javascript">
    //var urlsOccur = "${urlsOccur}";
    //alert(urlsOccur);
    //setup("${urlsOccur}");//creates the tag cloud
</script>
<div id="mytagcloud"></div>   