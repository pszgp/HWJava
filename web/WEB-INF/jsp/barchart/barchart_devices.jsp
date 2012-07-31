
  <link rel='stylesheet' href='js/michael-dance-a9121c3/examples/barchart/barchart.css'>
  <script src='http://documentcloud.github.com/underscore/underscore.js'></script>
  <script src='https://raw.github.com/michael/data/a38f5fd92a5490dc5ab6a2e95e88ecba2e644c71/data.js'></script>
  <script src='http://code.jquery.com/jquery-1.7.2.min.js'></script>
  <script src='https://raw.github.com/michael/dance/96cb9a6384acce19202275c6dce9b7fbdac87763/dance.js'></script>

    <!-- Devices data -->
  <!--inspered by: script src='js/michael-dance-a9121c3/barchart/countries.js'></script-->
  <!-- Dance Performers -->
  <script src="js/michael-dance-a9121c3/examples/barchart/barchart.js"></script>

  <script>    
    $(function() {
      var devices_data = {
        "type": {
            "_id": "/type/device",
            "name": "Devices",
            "properties": {
            "name": {"name": "Device Name", "type": "string" },
            "uri": {"name": "Visited websites", "type": "string" },
            "nbytes": { "name": "Number of bytes", "type": "number" },
            "nuris": { "name": "Number of visited websites", "type": "number" }
            },
            "indexes": {
            "by_name": ["name"]
            }
        },
        "objects": [
            {
            "_id": "d1",
            "name": "Device_1",
            "devices": ["google.co.uk", "mrl.nott.ac.uk"],
            "nbytes": 83,
            "nuris": 20
            },
            {
            "_id": "d2",
            "name": "Device_2",
            "languages": ["google.co.uk"],
            "nbytes": 82,
            "nuris": 10
            },
            {
            "_id": "d3",
            "name": "Device_3",
            "languages": ["google.co.uk", "microsoft.com", "guardian.co.uk", "linkedin.com", "bbc.co.uk"],
            "nbytes": 311,
            "nuris": 50
            },
            {
            "_id": "d4",
            "name": "Device_4",
            "languages": ["microsoft.com"],
            "nbytes": 62.3,
            "nuris": 10
            },
            {
            "_id": "d5",
            "name": "Device_5",
            "languages": ["guardian.co.uk"],
            "nbytes": 30.6,
            "nuris": 10
            },
            {
            "_id": "d6",
            "name": "Device_6",
            "languages": ["microsoft.com", "bbc.co.uk", "guardian.co.uk"],
            "nbytes": 40.1,
            "nuris": 30
            }
        ]
        };  
      var devices = new Data.Collection(devices_data);
      window.barchart = new Barchart({});

      barchart.update(devices, "nbytes");

      // Update
      function update() {
        var uri = $('#uri').val();
        alert(uri);
        var query = {};
        if (uri) query["devices"] = [ uri ];
        var items = devices.find(query);
        alert(items);
        barchart.update(items, $('#property').val());
      }

      $('#property').change(update);
      $('#uri').change(update);
    });
  </script>

<div id='container'>
    <select id="property">
      <option value="nbytes">Data usage (Mb, per device)</option>
      <option value="nuris">Number of visited websites (per device)</option>
    </select>
    <select id="uri">
      <option value="">All</option>
      <option value="uri_1">bbc.co.uk</option>
      <option value="uri_2">google.co.uk</option>
      <option value="uri_3">guardian.co.uk</option>
      <option value="uri_4">linkedin.com</option>
      <option value="uri_5">microsoft.com</option>
      <option value="uri_6">mrl.nott.ac.uk</option>
    </select>
    <div id='canvas'></div>
  </div>
  
  