
  <link rel='stylesheet' href='js/michael-dance-a9121c3/examples/barchart/barchart.css'>
  <script src='http://documentcloud.github.com/underscore/underscore.js'></script>
  <script src='https://raw.github.com/michael/data/a38f5fd92a5490dc5ab6a2e95e88ecba2e644c71/data.js'></script>
  <script src='http://code.jquery.com/jquery-1.7.2.min.js'></script>
  <script src='https://raw.github.com/michael/dance/96cb9a6384acce19202275c6dce9b7fbdac87763/dance.js'></script>

    <!-- Countries data -->
  <!--script src='js/michael-dance-a9121c3/barchart/countries.js'></script-->
  <!-- Dance Performers -->
  <script src="js/michael-dance-a9121c3/examples/barchart/barchart.js"></script>

  <script>    
    $(function() {
      var countries_data = countries_data = {
        "type": {
            "_id": "/type/country",
            "name": "Countries",
            "properties": {
            "name": {"name": "Country Name", "type": "string" },
            "languages": {"name": "Languages spoken", "type": "string" },
            "population": { "name": "Population", "type": "number" },
            "gdp": { "name": "GDP per capita", "type": "number" }
            },
            "indexes": {
            "by_name": ["name"]
            }
        },
        "objects": [
            {
            "_id": "at",
            "name": "Austria",
            "languages": ["German", "Austrian"],
            "population": 8.3,
            "gdp": 41.805
            },
            {
            "_id": "de",
            "name": "Germany",
            "languages": ["German"],
            "population": 82,
            "gdp": 46.860
            },
            {
            "_id": "us",
            "name": "United States of America",
            "languages": ["German", "English", "Spanish", "Chinese", "French"],
            "population": 311,
            "gdp": 36.081
            },
            {
            "_id": "uk",
            "name": "United Kingdom",
            "languages": ["English", "Irish", "Scottish Gaelic"],
            "population": 62.3,
            "gdp": 36.081
            },
            {
            "_id": "es",
            "name": "Spain",
            "languages": ["Spanish"],
            "population": 30.6,
            "gdp": 36.081
            },
            {
            "_id": "gr",
            "name": "Greece",
            "languages": ["Greek"],
            "population": 11.0,
            "gdp": 36.081
            },
            {
            "_id": "ca",
            "name": "Canada",
            "languages": ["English", "French", "Spanish"],
            "population": 40.1,
            "gdp": 40.457
            }
        ]
        };  
      var countries = new Data.Collection(countries_data);
      window.barchart = new Barchart({});

      barchart.update(countries, "gdp");

      // Update
      function update() {
        var language = $('#language').val();
        var query = {};
        if (language) query["languages"] = [ language ];
        var items = countries.find(query);
        barchart.update(items, $('#property').val());
      }

      $('#property').change(update);
      $('#language').change(update);
    });
  </script>

<div id='container'>
    <select id="property">
      <option value="gdp">GDP (thousands, per capita)</option>
      <option value="population">Population (millions)</option>
    </select>
    <select id="language">
      <option value="">All</option>
      <option value="English">English</option>
      <option value="French">French</option>
      <option value="German">German</option>
      <option value="Greek">Greek</option>
      <option value="Spanish">Spanish</option>
      <option value="Scottish Gaelic">Scottish Gaelic</option>
    </select>
    <div id='canvas'></div>
  </div>