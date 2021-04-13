package com.sbs.deungbulproto.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.deungbulproto.dao.ReviewDao;
import com.sbs.deungbulproto.dto.Adm;
import com.sbs.deungbulproto.dto.ResultData;
import com.sbs.deungbulproto.dto.Review;
import com.sbs.deungbulproto.util.Util;

@Service
public class ReviewService {
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private AdmMemberService admMemberService;
	@Autowired
	private RatingService ratingService;

	public ResultData addReview(Map<String, Object> param) {
		reviewDao.addReview(param);

		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", "리뷰가 작성되었습니다.", "id", id);
	}

	public List<Review> getForPrintReviews(String relTypeCode) {
		return reviewDao.getForPrintReviews(relTypeCode);
	}

	public Review getReview(int id) {
		return reviewDao.getReview(id);
	}

	public ResultData getActorCanDeleteRd(Review review, Adm actor) {
		if (review.getClientId() == actor.getId()) {
			return new ResultData("S-1", "가능합니다.");
		}

		if (admMemberService.isAdmin(actor)) {
			return new ResultData("S-2", "가능합니다.");
		}

		return new ResultData("F-1", "권한이 없습니다.");
	}

	public ResultData deleteReview(Map<String, Object> param) {
		int id = Util.getAsInt(param.get("id"), 0);
		reviewDao.deleteReview(id);

		ratingService.deleteRating(param);

		return new ResultData("S-1", "삭제하였습니다.", "id", id);
	}

	public ResultData getActorCanModifyRd(Review review, Adm actor) {
		return getActorCanDeleteRd(review, actor);
	}

	public boolean isClientCanReview(int clientId, int relId) {
		Review review = reviewDao.getReviewByClientIdAndRelId(clientId, relId);
		if (review != null) {
			return false;
		}
		return true;
	}

	public ResultData modifyReview(Map<String, Object> param) {
		reviewDao.modifyReview(param);
		return new ResultData("S-1", "리뷰를 수정하였습니다.");
	}

	public ResultData getForPrintReview(Integer id) {
		Review review = reviewDao.getForPrintReview(id);
		return new ResultData("S-1", "성공", "review", review);
	}

	public List<Review> getForPrintReviewsByExpertId(String relTypeCode, Integer relId) {
		return reviewDao.getForPrintReviewsByExpertId(relTypeCode, relId);
	}

}
