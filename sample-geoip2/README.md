### sample-GEOIP2
此模块整合GeoIP2，实现根据ip地址获取对应国家、省份、城市、经纬度等
- 官网：https://www.maxmind.com/en/home
- 下载数据：https://dev.maxmind.com/geoip/geoip2/geolite2/#Downloads
- JavaAPI：https://maxmind.github.io/GeoIP2-java/

#### 依赖
- https://search.maven.org/search?q=a:geoip2
```
<dependency>
    <groupId>com.maxmind.geoip2</groupId>
    <artifactId>geoip2</artifactId>
    <version>2.12.0</version>
</dependency>
```

#### 运行
- 运行GeoIP2Application
- 访问: http://localhost:10090/sample-geoip2/swagger-ui.html
- 通过swagger界面调用接口，输入ip（如：128.101.101.101）,响应如下：
```
{
  "countryIsoCode": "US",
  "countryName": "United States",
  "countryNames": "美国",
  "subdivisionName": "Minnesota",
  "subdivisionIsoCode": "MN",
  "cityName": "Saint Paul",
  "postalCode": "55112",
  "locationLatitude": 45.0782,
  "locationLongitude": -93.1894
}
```

#### 示例代码
```
File database = new File("/path/to/GeoIP2-City.mmdb");

DatabaseReader reader = new DatabaseReader.Builder(database).build();

InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

CityResponse response = reader.city(ipAddress);

Country country = response.getCountry();
System.out.println(country.getIsoCode());            // 'US'
System.out.println(country.getName());               // 'United States'
System.out.println(country.getNames().get("zh-CN")); // '美国'

Subdivision subdivision = response.getMostSpecificSubdivision();
System.out.println(subdivision.getName());    // 'Minnesota'
System.out.println(subdivision.getIsoCode()); // 'MN'

City city = response.getCity();
System.out.println(city.getName()); // 'Minneapolis'

Postal postal = response.getPostal();
System.out.println(postal.getCode()); // '55455'

Location location = response.getLocation();
System.out.println(location.getLatitude());  // 44.9733
System.out.println(location.getLongitude()); // -93.2323
```