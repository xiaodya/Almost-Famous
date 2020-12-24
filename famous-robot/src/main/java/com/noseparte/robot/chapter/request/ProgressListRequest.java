package com.noseparte.robot.chapter.request;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.ChapterBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.resources.ChapterConf;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.chapter.cmd.ProgressListCmd;
import com.noseparte.robot.enitty.Chapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/9/17 10:28
 * @Description
 */
@Slf4j
public class ProgressListRequest extends RequestSync {

    ProgressListCmd progressListCmd;

    public ProgressListRequest(ProgressListCmd progressListCmd) {
        this.progressListCmd = progressListCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, progressListCmd.toKeyValuePair(), new ProgressListResponse());
    }

    private class ProgressListResponse implements ResponseCallBack<HttpResponse> {
        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            String data = object.getString("data");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                Long rid = dataObj.getLong("rid");
                String chapterIds = dataObj.getString("chapterIds");

                List<Integer> chapterIdLst = FastJsonUtils.toList(chapterIds, Integer.class);
                if (log.isDebugEnabled()) {
                    log.debug("rid {}, 已通关的章节列表 {}", rid, chapterIdLst);
                }
                // 章节列表
                Map<Integer, ChapterConf> chapterConfMap = ConfigManager.chapterConfMap;
                Map<Integer, ChapterBean> chapterMap = new HashMap<Integer, ChapterBean>();
                RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
                Robot robot = robotMgr.getRobotMap().get(rid);
                Chapter chapter = new Chapter();
                chapter.setRid(rid);
                chapter.setChapters(chapterMap);
                robot.setChapter(chapter);


            }
        }

        @Override
        public void failed(Exception ex) {

        }

        @Override
        public void cancelled() {

        }
    }
}
