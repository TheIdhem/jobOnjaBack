package ir.joboona.Presentation.ControllerAdvices;

import Solutions.Presentation.ControllerAdvice.RestControllerAdvice;
import Solutions.Presentation.ControllerAdvice.RestControllerAdviceHandler;
import ir.joboona.Exceptions.BidExceptions.BudgetOverflow;
import ir.joboona.Exceptions.BidExceptions.IllegalBidException;
import ir.joboona.Exceptions.BidExceptions.InsufficientSkill;

@RestControllerAdvice
public class BidControllerAdvice {

    @RestControllerAdviceHandler(httpStatus = 400)
    public IllegalBidExceptionDto handle(BudgetOverflow e){
        return new IllegalBidExceptionDto("مبلغ پیشنهادی شما بیشتر از بودجه است");
    }

    @RestControllerAdviceHandler(httpStatus = 400)
    public IllegalBidExceptionDto handle(InsufficientSkill e){
        return new IllegalBidExceptionDto("شما مهارت های کافی را برای اخذ این پروژه ندارید");
    }

    public static class IllegalBidExceptionDto {
        private String message;

        public IllegalBidExceptionDto() {
        }

        IllegalBidExceptionDto(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
