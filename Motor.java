class Motor {
    double motorPower;

    void setMotorPower(double power){
        motorPower = power;
    }

    double getMotorSpeed(){
        return motorPower * 0.415;
    }
}