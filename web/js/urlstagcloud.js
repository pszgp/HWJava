function setuptc(urlsOccur, tagcloudElement)
{
    
    var tc = TagCloud.create();

    //pszgp: use an existing tagcloud function
        //  to add the urls to the tag, then call the existing tagcloud function 
    
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
            var url = {};
            var name = data[i];
            var occur = data[i];

            name = name.substring(0, name.indexOf("="));

            occur = occur.substring(occur.indexOf("=")+1, occur.length);
            occur = +occur;
            if (occur > 1000)
                occur/=10;
            else if ((occur < 50) && (occur > 10))
                occur *= 5;
            else if (occur < 10)
                occur *= 50;

            url.name = name;
            url.occur = occur;

            //
            //4 sept 2012: use as url the urls.htm?url=name
            url.href = "urls.htm?url="+url.name;

            tc.add(url.name, url.occur, url.href, Date.parse('005/06/23 00:00:00'));

        }
    }


    tc.loadEffector('CountSize').base(10).range(5);
    tc.loadEffector('DateTimeColor');

    tc.setup(tagcloudElement);//'mytagcloud');    
}