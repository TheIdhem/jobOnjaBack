package ir.joboona.Presentation.Controllers.ControllerAdvices.Common;

import Solutions.Data.Exceptions.EntityNotFound;

public class EntityNotFoundDto{

        private EntityNotFound error;

        public EntityNotFoundDto(){}

        public EntityNotFoundDto(EntityNotFound error) {
            this.error = error;
        }

        public String getMessage(){
            return "خطا در هویت ماهیت درخواست شده";
        }
    }