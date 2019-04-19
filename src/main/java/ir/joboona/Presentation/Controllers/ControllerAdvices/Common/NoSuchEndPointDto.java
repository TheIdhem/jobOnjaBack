package ir.joboona.Presentation.Controllers.ControllerAdvices.Common;

import Solutions.Core.Exceptions.NoSuchEndPoint;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class NoSuchEndPointDto{


        private NoSuchEndPoint error;

        public NoSuchEndPointDto(){}

        public NoSuchEndPointDto(NoSuchEndPoint error) {
            this.error = error;
        }

        public String getMessage(){
            return "خطا در مسیر درخواست";
        }
    }