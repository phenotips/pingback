# PhenoTips pingback Mechanism

This has been forked from [xwiki-platform](https://github.com/xwiki/xwiki-platform/tree/master/xwiki-platform-core/xwiki-platform-activeinstalls)
due to PhenoTips not using the latest version of XWiki.

## Pingback ElasticSearch Configuration 

### Development

To quickly set up a running ES instance, make sure you have docker and
docker-compose install. Then run `docker-compose up` in this directory.

### Production

- Install `./elasticsearch/elasticsearch.yml` as the config file for your ES instance.
- Install
  [elasticsearch-readonlyrest-plugin](https://github.com/sscarduzio/elasticsearch-readonlyrest-plugin)
- It is recommended to use an ES interface like
  [Kibana](https://www.elastic.co/products/kibana) to browse and graph the ES
  indeces. **Note** if Kibana is not running on the same machine as ES, make
  sure to allow it's IP in the `readonlyrest` section of `elasticsearch.yml`
