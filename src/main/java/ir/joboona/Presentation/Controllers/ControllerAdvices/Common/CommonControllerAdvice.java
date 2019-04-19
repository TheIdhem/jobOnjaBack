package ir.joboona.Presentation.Controllers.ControllerAdvices.Common;

import Solutions.Core.Exceptions.IllegalFormat;
import Solutions.Core.Exceptions.MissingParameter;
import Solutions.Core.Exceptions.NoSuchEndPoint;
import Solutions.Data.Exceptions.EntityNotFound;
import Solutions.Presentation.ControllerAdvice.RestControllerAdvice;
import Solutions.Presentation.ControllerAdvice.RestControllerAdviceHandler;
import ir.joboona.Exceptions.BidExceptions.Common.Unauthorized;

@RestControllerAdvice
public class CommonControllerAdvice {


    @RestControllerAdviceHandler(httpStatus = 404)
    public NoSuchEndPointDto handleNoSuchEndPoint(NoSuchEndPoint e){
        return new NoSuchEndPointDto(e);
    }

    @RestControllerAdviceHandler(httpStatus = 422)
    public EntityNotFoundDto handleEntityNotFound(EntityNotFound e){
        return new EntityNotFoundDto(e);
    }

    @RestControllerAdviceHandler(httpStatus = 403)
    public Unauthorized handleUnauthorized(Unauthorized e){
        return e;
    }

    @RestControllerAdviceHandler(httpStatus = 400)
    public MissingParameter handleMissingParameter(MissingParameter e){
        return e;
    }

    @RestControllerAdviceHandler(httpStatus = 400)
    public IllegalFormat handleMissingParameter(IllegalFormat e){
        return e;
    }

    @RestControllerAdviceHandler(httpStatus = 500)
    public Throwable handleAll(Throwable e){
        e.printStackTrace();
        return e;
    }

}