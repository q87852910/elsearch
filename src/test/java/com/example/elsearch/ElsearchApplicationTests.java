package com.example.elsearch;

import com.alibaba.fastjson.JSON;
import com.example.elsearch.User.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ElsearchApplicationTests {

	@Autowired
	@Qualifier("restHighLevelClient")
	private RestHighLevelClient client;

	//创建索引
	@Test
	void testCreatIndex() throws IOException {
		//创建
		CreateIndexRequest kuangIndex = new CreateIndexRequest("jingdong-list");
		CreateIndexResponse createIndexResponse = client.indices().create(kuangIndex, RequestOptions.DEFAULT);
		System.out.println(createIndexResponse);
	}
	//获取索引
	@Test
	void ExistIndex() throws IOException {
		GetIndexRequest kuang_index = new GetIndexRequest("kuang_index");
		boolean exists = client.indices().exists(kuang_index, RequestOptions.DEFAULT);
		System.out.println(exists);
	}
	//删除索引
	@Test
	void deleteIndex() throws IOException {
		DeleteIndexRequest kuang_index = new DeleteIndexRequest("jingdong-list");
		AcknowledgedResponse delete = client.indices().delete(kuang_index, RequestOptions.DEFAULT);
		System.out.println(delete.isAcknowledged());
	}

	//添加测试文档
	@Test
	void testDocument() throws IOException {
		User user = new User("张三", 20);
		IndexRequest request = new IndexRequest("kuang_index");
		//规则 put /kuang/_doc/1
		request.id("1");
		request.timeout(TimeValue.timeValueSeconds(2));
		//将数据放到对象
		request.source(JSON.toJSONString(user), XContentType.JSON);
		//客户端发送请求
		IndexResponse index = client.index(request, RequestOptions.DEFAULT);
		System.out.println(index.toString());
		System.out.println(index.status());

	}
	//获取文档，判断是否存在
	@Test
	void testisExists() throws IOException {
		GetRequest request = new GetRequest("kuang_index", "1");
		request.fetchSourceContext(new FetchSourceContext(false));
		request.storedFields("_none_");
		boolean exists = client.exists(request, RequestOptions.DEFAULT);
		System.out.println(exists);
	}
	//获取文档信息
	@Test
	void testGetDoc() throws IOException {
		GetRequest request = new GetRequest("kuang_index", "1");
		GetResponse response = client.get(request, RequestOptions.DEFAULT);
		System.out.println(response.getSourceAsString());
	}
	//更新文档
	@Test
	void testUpdateDoc() throws IOException {
		UpdateRequest request = new UpdateRequest("kuang_index", "1");
		request.timeout("1s");
		User user = new User("狂神说java", 18);
		request.doc(JSON.toJSONString(user),XContentType.JSON);
		UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
		System.out.println(update.status());
	}
	//删除文档
	@Test
	void testDeleteDoc() throws IOException {
		DeleteRequest request = new DeleteRequest("kuang_index", "1");
		request.timeout("1s");
		DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
		System.out.println(delete.status());

	}

	//批量插入
	@Test
	void testBulkInsert() throws IOException {
		BulkRequest request = new BulkRequest();
		request.timeout("1s");
		List<User> list =  new ArrayList<>();
		list.add(new User("张三",1));
		list.add(new User("张三2",2));
		list.add(new User("张三3",3));
		list.add(new User("张三4",4));
		list.add(new User("张三5",5));
		for(int i=0;i<list.size();i++){
			request.add(new IndexRequest("kuang_index").id(""+(i+1)).
					source(JSON.toJSONString(list.get(i)),XContentType.JSON));
			client.bulk(request,RequestOptions.DEFAULT);
		}
	}

	//查询
	@Test
	void  testSearch() throws IOException {
		SearchRequest searchRequest = new SearchRequest("kuang_index");
		SearchSourceBuilder builder = new SearchSourceBuilder();
		TermQueryBuilder query = QueryBuilders.termQuery("name", "张三");
		builder.timeout(new TimeValue(60, TimeUnit.SECONDS));
		builder.query(query);
		searchRequest.source(builder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		System.out.println(JSON.toJSONString(response.getHits()));
		System.out.println(response.getHits().getHits()[0].getSourceAsMap());
	}
}
