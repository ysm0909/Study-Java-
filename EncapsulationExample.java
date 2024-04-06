package kr.easw.lesson4;

/**
 * 해당 클래스는 Car을 상속받아 연비 값을 확인하는 코드를 갖고 있습니다.
 *
 * 이 코드에서는 세터에 아무런 필터링이 존재하지 않아 이 값을 검증할 수 없습니다.
 *
 * {@link Car} 클래스만을 수정해 {@link PerformanceManipulation} 클래스에서 수행하는 값 조작을 막아야 합니다.
 *
 * 이는 다음의 조건을 따라야 합니다 :
 * - 현재 연비보다 높은 값을 설정하려 할 경우, 종류 상관 없이 오류를 발생시켜야 합니다.
 *
 * 오류는 throw new 구문으로 발생시킬 수 있습니다.
 */
public class EncapsulationExample {
    public static void main(String[] args) {
        try {
            Car car = getCar();
            System.out.printf("차 이름: %s\n", car.carName);
            System.out.printf("연비: %.2fL/h", car.realFuelEfficiency);
            System.out.println("오답입니다.");
        } catch (Exception e) {
            System.out.println("연비 조작이 확인되었습니다.");
            System.out.println("정답입니다.");
        }
    }

    private static abstract class Car {
        private final String carName = "Car Prototype";

        private double realFuelEfficiency = 7.5;

        public String getCarName() {
            return carName;
        }

        public double getRealFuelEfficiency() {
            return realFuelEfficiency;
        }

        public void setRealFuelEfficiency(double realFuelEfficiency) {
        	if (realFuelEfficiency > this.realFuelEfficiency) {
                throw new RuntimeException("현재 연비보다 높은 값을 설정할 수 없습니다.");
            }
        	
        	this.realFuelEfficiency = realFuelEfficiency;
        }
    }

    private static class PerformanceManipulation extends Car {
        {
            setRealFuelEfficiency(15.0);
        }

        @Override
        public String getCarName() {
            return "New Car";
        }

    }


    public static Car getCar() {
        return new PerformanceManipulation();
    }
}