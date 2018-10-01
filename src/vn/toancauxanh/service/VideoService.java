package vn.toancauxanh.service;

import java.util.HashMap;
import java.util.Map;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.QVideo;
import vn.toancauxanh.model.Video;

public class VideoService extends BasicService<Video>{
	public Map<String , String> getListTypeVideo() {
		HashMap<String, String> map = new HashMap<>();
		map.put("tep-tin", "Video tải lên từ máy");
		map.put("link", "Link Youtbe");
		return map;
	}
	
	public JPAQuery<Video> getTargetQueryById(Long id) {
		JPAQuery<Video> q = find(Video.class);
		q.where(QVideo.video.id.eq(id));
		q.orderBy(QVideo.video.id.desc());
		return q;
	}
}