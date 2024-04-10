package org.example.controller;

import org.example.container.Container;
import org.example.dto.Booking;
import org.example.dto.Guest;
import org.example.dto.Room;
import org.example.service.BookingService;
import org.example.util.Util;

import java.util.List;
import java.util.Scanner;

public class BookingController extends Controller {
    private Scanner sc;
    private String cmd;
    private List<Room> rooms;
    private BookingService bookingService;
    private Session session;

    public BookingController() {
        sc = new Scanner(System.in);
        rooms = Container.roomDao.rooms;
        bookingService = Container.bookingService;
        session = Container.getSession();
    }

    public void doAction(String cmd, String actionMethodName) {
        this.cmd = cmd;
        
        switch(actionMethodName) {
            case "room" :
                doBooking();
                break;
            case "check" :
                doCheckBooking();
                break;
            case "delete" :
                doDeleteBooking();
                break;
            case "reply" :
                doWriteReview();
                break;
            default :
                System.out.println("존재하지 않는 서비스입니다.");
                break;
        }
    }

    public void doBooking() {
        String[] cmdBits = cmd.split(" ");
        if(cmdBits.length > 2) {
            System.out.println("존재하지 않는 서비스입니다.");
            return;
        }

        // 로그인 된 게스트 가져오기
        Guest loginedGuest = session.getLoginedGuest();

        System.out.print("예약할 객실 호수 입력(숫자만) : ");
        String roomNum = sc.nextLine();

        String[] roomNumBits = roomNum.split("");
        int floor = Integer.parseInt(roomNumBits[0]);
        int number = Integer.parseInt(roomNumBits[2]);

        // 날짜 입력 및 7일간 날짜 판단
        System.out.print("예약 날짜 입력) ");
        String bookingDate = sc.nextLine();
        bookingDate = bookingDate.trim();

        if(Util.checkWeekDate(bookingDate) == false) {
            System.out.println("오늘 날짜부터 7일간 조회 가능합니다.");
            return;
        }

        // getBoookingAbleRoom에서 가져오기
        // 호수가 같고, 날짜가 같은 조건 넣어서 select하기
        Room bookingAbleRoom = Container.roomService.getBookingAbleRoom(floor, number, bookingDate);

        if(bookingAbleRoom == null) {
            System.out.println("선택하신 객실은 존재하지 않습니다.");
        }

        else if(bookingAbleRoom.booked.equals("예약불가")) {
            System.out.println("예약이 이미 완료된 객실입니다.");
        }

        else {
            // 요금 안내
            int payment = 150000;
            int count = 0;
            int plusPay = 20000;

            // 객실 타입 안내
            if(bookingAbleRoom.roomNum % 2 == 1) {
                System.out.print("인원은 몇 명 이신가요?) ");
                int peopleNum = sc.nextInt();
                sc.nextLine();

                if(peopleNum > 2) {
                    count = peopleNum - 2;
                    System.out.println("[싱글] 정원 2명을 초과하셨습니다!!");
                    System.out.println("추가 인원 당 20,000원의 추가 요금이 발생합니다!!");
                }
            }
            else if(bookingAbleRoom.roomNum % 2 == 0) {
                payment += 100000;
                System.out.print("인원은 몇 명 이신가요?) ");
                int peopleNum = sc.nextInt();
                sc.nextLine();

                if(peopleNum > 4) {
                    count = peopleNum - 4;
                    System.out.println("[더블] 정원 4명을 초과하셨습니다!!");
                    System.out.println("추가 인원 당 20,000원의 추가 요금이 발생합니다!!");
                }
            }

            System.out.printf("총 금액은 [%,d원] 입니다.\n", (payment+(count*plusPay)));

            while(true) {
                System.out.print("예약을 진행할까요?) ");
                String answer = sc.nextLine();
                answer = answer.trim();

                if(answer.equals("yes")) {
                    bookingService.add(Integer.parseInt(roomNum), bookingDate, loginedGuest.name, loginedGuest.phoneNum, bookingAbleRoom.type, (payment+(count*plusPay)));

                    // bookingAbleRoom 상태를 예약불가로 변경
                    Container.roomService.setBookingComplete(floor, number, bookingDate);

                    // 로그인된 회원의 이름으로 예약 성공
                    System.out.printf("[%s]님 예약 성공하셨습니다!! 결제는 당일 카운터에서 진행 부탁드립니다!\n", loginedGuest.name);
                    break;
                }

                else if(answer.equals("no")) {
                    System.out.println("예약을 중단합니다.");
                    break;
                }

                else {
                    System.out.println("\'yes\' 또는 \'no\'를 입력해주세요");
                    continue;
                }
            }
        }
    }

    public void doCheckBooking() {
        // 로그인 된 게스트 가져오기
        Guest loginedGuest = session.getLoginedGuest();

        // 로그인된 회원이 예약한 목록 가져오기
        List<Booking> forPrintBookings = bookingService.getForPrintBookings(loginedGuest.name);

        int foundBookingCount = forPrintBookings.size();
        System.out.printf("%s 님, 총 [%d]건의 예약이 있습니다.\n", loginedGuest.name, foundBookingCount);

        if (foundBookingCount >= 1) {
            System.out.print("예약 상세보기를 진행 하시겠습니까?) ");
            String answer = sc.nextLine();

            while (true) {
                if (answer.equals("yes")) {
                    System.out.printf("==== [%s 님] 예약 현황 =======\n", loginedGuest.name);
                    System.out.println("호수 | 객실타입 | 결제요금 | 체크인날짜");

                    // 예약한 목록 출력
                    for(int i = 0; i < forPrintBookings.size(); i++) {
                        Booking bookingByGuest = forPrintBookings.get(i);

                        if(bookingByGuest.roomId % 2 == 1) {
                            System.out.printf("%d  |     싱글 |  %,d | %s\n", bookingByGuest.roomId, bookingByGuest.bookingPay, bookingByGuest.regDate);
                        }
                        else {
                            System.out.printf("%d  |     더블 |  %,d | %s\n", bookingByGuest.roomId, bookingByGuest.bookingPay, bookingByGuest.regDate);
                        }
                    }

                    System.out.println("====================================");
                    break;
                }
                else if (answer.equals("no")) {
                    System.out.println("상세보기를 건너뜁니다.");
                    break;
                }
                else {
                    System.out.println("\'yes\' 또는 \'no\'를 입력해주세요");
                    return;
                }
            }
        }
    }

    public void doDeleteBooking() {
        // 로그인 된 게스트 가져오기
        Guest loginedGuest = session.getLoginedGuest();

        // 로그인된 회원이 예약한 목록 가져오기
        List<Booking> forPrintBookings = bookingService.getForPrintBookings(loginedGuest.name);

        int foundBookingCount = forPrintBookings.size();
        System.out.printf("%s 님, 총 [%d]건의 예약이 있습니다.\n", loginedGuest.name, foundBookingCount);

        if(foundBookingCount == 0) {
            System.out.println("취소하실 예약 건이 없습니다.");
        }
        else {
            System.out.printf("==== [%s 님] 예약 현황 =======\n", loginedGuest.name);
            System.out.println("예약번호 | 호수 | 체크인날짜");

            // 예약한 목록 출력
            for(int i = 0; i < forPrintBookings.size(); i++) {
                Booking bookingByGuest = forPrintBookings.get(i);

                System.out.printf("    %4d |  %d | %s\n", bookingByGuest.id, bookingByGuest.roomId, bookingByGuest.regDate);
            }

            System.out.println("====================================");
            System.out.print("예약을 취소하시겠습니까?) ");
            String answer = sc.nextLine();

            if(answer.equals("yes")) {
                System.out.print("취소하시고 싶은 예약번호를 입력(숫자만) : ");
                int answerId = sc.nextInt();
                sc.nextLine();

                // 예약번호에 따른 booking 가져오기
                Booking forPrintBooking = bookingService.getForPrintBooking(answerId);

                // 객실번호(303과 같은)를 100으로 나눴을 때 몫을 층 수
                int floor = forPrintBooking.roomId / 100;
                // 객실번호(303과 같은)를 100으로 나눴을 때 나머지를 호수
                int number = forPrintBooking.roomId % 10;

                // 삭제할 forPrintBooking을 인자로 넘겨 room 정보 수정하기
                Container.roomService.setBooingDelete(floor, number);
                // 예약목록에서 삭제
                bookingService.deleteBooking(answerId);

                System.out.println("예약이 취소되었습니다!!");
            }
            else if(answer.equals("no")) {
                System.out.println("예약취소를 중단합니다.");
            }
            else {
                System.out.println("\'yes\' 또는 \'no\'를 입력해주세요");
            }
        }
    }

    public void doWriteReview() {
    }
}
