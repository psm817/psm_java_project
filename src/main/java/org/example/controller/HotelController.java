package org.example.controller;

public class HotelController extends Controller {
    public void doAction(String cmd, String actionMethodName) {
        switch(actionMethodName) {
            case "introduce" :
                doIntroduce();
                break;
            default :
                System.out.println("존재하지 않는 서비스입니다.");
        }
    }

    private void doIntroduce() {
        System.out.printf("\n");
        System.out.println("=================== 호텔 소개 =====================");
        System.out.println("호텔명 : 대전 그랜드 호텔");
        System.out.println("호텔 위치 : 대전광역시 서구 청사로 111 (3 ~ 5층)");
        System.out.println("호텔 전화번호 : 042-123-1234");
        System.out.println("카운터 영업시간 : 오전 8시 ~ 오후 8시");
        System.out.println("객실 구성 : 층별 5개");
        System.out.println("객실 종류 : 싱글(홀수) / 더블(짝수)");
        System.out.println("객실 수용 인원 : 싱글(2인) / 더블(4인)");
        System.out.println("객실 요금 : 싱글(150,000원) / 더블(250,000원)");
        System.out.println("체크인 시간 : 오후 3시 / 체크아웃 시간 : 오전 11시");
        System.out.println("** 객실 예약은 일주일 범위 내에서만 가능 **");
        System.out.println("** 인원 초과 시, 인당 20,000원 추가 **");
        System.out.println("===================================================");
        
        System.out.println("=================== 객실 배치 =====================");
        System.out.println("*** 층별로 위치는 동일 ***");
        System.out.println("━━━━━┓\n" +
                "┓┓┓┓┓┃         ┎━━━━━━┒      ┎━━━━━━┒      ┎━━━━━━┒\n" +
                "┓┓┓┓┓┃         ┃┏┓┏┓┏┓┃      ┃┏┓┏┓┏┓┃      ┃┏┓┏┓┏┓┃\n" +
                "┓┓┓┓┓┃         ┃┏┓┏┓┏┓┃      ┃┗┛┗┛┗┛┃      ┃┗┛┗┛┗┛┃\n" +
                "┓┓┓┓┓┃         ┃┏┓┏┓┏┓┃      ┃┏┓┏┓┏┓┃      ┃┏┓┏┓┏┓┃\n" +
                "┓┓┓┓┓┃ 엘         1호           3호           5호\n" +
                "┓┓┓┓┓┃ 리\n" +
                "┓┓┓┓┓┃ 베\n" +
                "┓┓┓┓┓┃ 이\n" +
                "┓┓┓┓┓┃ 터          ┎━━━━━━━━━━┒    ┎━━━━━━━━━━┒\n" +
                "┓┓┓┓┓┃ 　          ┃┏┓┏┓┏┓┏┓┏┓┃    ┃┏┓┏┓┏┓┏┓┏┓┃\n" +
                "┓┓┓┓┓┃　           ┃┗┛┗┛┗┛┗┛┗┛┃    ┃┗┛┗┛┗┛┗┛┗┛┃\n" +
                "┓┓┓┓┓┃　           ┃┏┓┏┓┏┓┏┓┏┓┃    ┃┏┓┏┓┏┓┏┓┏┓┃\n" +
                "┓┓┓┓┓┃                 2호              4호");
        System.out.println("===================================================");

    }

}
