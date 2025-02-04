package com.bigp.back.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bigp.back.entity.AdminInfo;
import com.bigp.back.entity.NoticeInfo;
import com.bigp.back.repository.AdminRepository;
import com.bigp.back.repository.NoticeRepository;
import com.bigp.back.utils.DateFormatter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeInfoService {
    private final AdminRepository adminRepository;
    private final NoticeRepository noticeRepository;
    private final AdminInfoService adminService;
    private final DateFormatter transDate;

    public boolean insertNotice(String accessToken, String header, String body, String footer) {
        AdminInfo admin = adminService.getAdminInfo(accessToken);

        if (admin != null) {
            NoticeInfo notice = new NoticeInfo();
            notice.setHeader(header);
            notice.setBody(body);
            notice.setFooter(footer);
            notice.setNotice(admin);
            notice.setWritetime(new Date());
            noticeRepository.save(notice);

            List<NoticeInfo> list = admin.getNotice();
            list.add(notice);
            admin.setNotice(list);
            adminRepository.save(admin);
            return true;
        }
        return false;
    }

    public boolean updateNotice(String accessToken, String header, String body, String footer) {
        AdminInfo admin = adminService.getAdminInfo(accessToken);

        if (admin != null) {
            List<NoticeInfo> list = admin.getNotice();
            for (NoticeInfo notice: list) {
                if (notice.getHeader().equals(header)) {
                    notice.setBody(body);
                    notice.setFooter(footer);
                    notice.setWritetime(new Date());
                    noticeRepository.save(notice);
                    break;
                }
            }
            admin.setNotice(list);
            adminRepository.save(admin);
            return true;
        }
        return false;
    }

    public boolean deleteNotice(String accessToken, String header, String writetime) {
        AdminInfo admin = adminRepository.findByAccessToken(accessToken);

        if (admin != null) {
            List<NoticeInfo> list = admin.getNotice();

            for (NoticeInfo notice: list) {
                if (notice.getHeader().equals(header)
                    && transDate.formatDateYMDHMS(notice.getWritetime()).equals(writetime)) {
                    list.remove(notice);
                    noticeRepository.delete(notice);
                    break;
                }
            }
            admin.setNotice(list);
            adminRepository.save(admin);
            return true;
        }
        return false;
    }

    public List<Map<String, String>> getNoticeList() {
        AdminInfo admin = adminRepository.findAll().getFirst();

        if (admin != null) {
            List<NoticeInfo> list = admin.getNotice().reversed();

            List<Map<String, String>> headerList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                Map<String, String> sendList = new HashMap<>();

                sendList.put("id", String.valueOf(i));
                sendList.put("header", list.get(i).getHeader());
                sendList.put("time", transDate.formatDateYMDHMS(list.get(i).getWritetime()));

                headerList.add(sendList);
            }
            return headerList;
        }
        return null;
    }

    public Map<String, String> readNotice(String header, String writetime) {
        AdminInfo admin = adminRepository.findAll().getFirst();

        if (admin != null) {
            List<NoticeInfo> list = admin.getNotice();
            Map<String, String> map = new HashMap<>();
            
            for (NoticeInfo notice: list) {
                if (notice.getHeader().equals(header)
                    && transDate.formatDateYMDHMS(notice.getWritetime()).equals(writetime)) {
                    map.put("header", notice.getHeader());
                    map.put("body", notice.getBody());
                    map.put("footer", notice.getFooter());
                    return map;
                }
            }
        }
        return null;
    }
}
