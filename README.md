# Microservicio REST

**Objetivo**

Simplificar el desarrollo de microservicios REST con SpringBoot para Claro. Se recomienda reemplazar ente README con
README-TEMPLATE actualizado con la informacion del proyecto.

**Estructura**

| Package     | Descripcion                                                  |
|-------------|--------------------------------------------------------------|
| business/   | Capa de negocio, clases relacionadas a la logica del negocio |
| client/     | Capa cliente, clases relacionadas al consumo de apis         |
| commons/    | Clases comunes al proyecto                                   |
| config/     | Configuraciones                                              |
| constants/  | Clases con utilidades o auxiliares                           |
| controller/ | Controladores                                                |
| dao/        | Capa acceso a datos                                          |
| mapper/     | Capa de mapeo de objetos                                     |
| model/      | Capa de modelo de clases                                     |
| service/    | Capa de servicios                                            |

**Links Utiles**

* [Documentacion](http://gracia.claro.amx:8090/display/CPRE/Starter+REST)

* [Validaciones](https://www.baeldung.com/javax-validation)

* [Resilience4j](https://resilience4j.readme.io/docs/getting-started-3)

* [Log ElasticSearch](http://gracia.claro.amx:8090/display/NPIYS/log4elk-spring-boot)

* [WebClient](https://www.baeldung.com/spring-5-webclient)

---

### Estructura

**business**

Componentes relacionados a la logica de negocio cuyo fin es verificar y validar los datos de cada objeto/tabla de la
base.

**config**

* `DataSourceConfig`: Contiene la configuracion de los datasource de la base CCARD y PROD.
* `MyBatisConfig`: Contiene la configuracion y administracion de los mappers de cada base CCARD y PROD, como asi tambien
  la generacion de su sesion.

**dao**

Componentes relacionados al acceso de cada objeto/tabla de la base.

**mapper**

Mappers encargado de convertir cada objeto/tabla de la base configurada en formato XML a clases JAVA.

**model**

Modelo de clases de cada objeto/tabla de la base.

**constants**

Contiene constantes, metodos estaticos que pueden ser reutilizados en el proyecto como asi tambien una clase de logueo
estandar pre-configurada.

### Informacion adicional

**Validaciones**

Las validaciones de datos que reciben los modelos se realizan a traves de anotaciones. Ej:

````
	@NotEmpty
	String unStringNoVacio;
	@Size(min = 3)
	String unStringDeAlMenos3Caracteres;
````

Otras validaciones de datos de entrada se realiza mediante las siguientes clases y metodos.

Ruta de la clase: `commons/resolver/CustomHeadersResolver`

Metodo: `public static void validateHeaders(Map<String, String> headersMap)`

Ruta de la clase: `commons/resolver/CustomInputResolver`

Metodo: `public static void validateInput(RechargeOfferRequest rechargeOfferRequest)`

**Resilience4j**

La clase donde se utiliza las configuraciones de la libreria resilience4j.

Ruta de la clase: `commons/resilience4j/Resilience4jService`

Ejemplo:

```
        resilience4jService.executeCellulars(() -> cellularsService.getCellularPlans(request));
```

Donde:
> * resilience4jService.executeCellulars: Metodo generado para asignar una configuración personalizada de la libreria resilience4j.
> * cellularsService.getCellularPlans: Metodo a encapsular con el fin de ejecutar las configuraciones realizadas en la libreria resilience4j.
> * request: Parámetros de entrada del metodo.

* **Circuit Breaker**

Para utilizar esta propiedad se debe asignar la anotacion `@CircuitBreaker` indicando tambien el nombre de la
configuracion en el metodo que corresponda encapsular.

Ejemplo:

```
    @CircuitBreaker(name = CELLULARS_API)
    public <T> T executeCellulars(Supplier<T> operation) {
        return operation.get();
    }
```

* **Rate Limiter**

Para utilizar esta propiedad se debe asignar la anotacion `@RateLimiter` indicando tambien el nombre de la configuracion
en el metodo que corresponda encapsular.

Ejemplo:

```
    @RateLimiter(name = CELLULARS_API)
    public <T> T executeCellulars(Supplier<T> operation) {
        return operation.get();
    }
```

* **Bulkhead**

Para utilizar esta propiedad se debe asignar la anotacion `@Bulkhead` indicando tambien el nombre de la configuracion en
el metodo que corresponda encapsular.

Ejemplo:

```
    @Bulkhead(name = CELLULARS_API)
    public <T> T executeCellulars(Supplier<T> operation) {
        return operation.get();
    }
```

* **Retry**

Para utilizar esta propiedad se debe asignar la anotacion `@Retry` indicando tambien el nombre de la configuracion en el
metodo que corresponda encapsular.

Ejemplo:

```
    @Retry(name = CELLULARS_API)
    public <T> T executeCellulars(Supplier<T> operation) {
        return operation.get();
    }
```

**Excepciones y Log a ElasticSearch**

Todas las excepciones y los tiempos de ejecución son capturados y enviados a ElasticSearch a traves del logueo por
aspecto.

Ruta de la clase: `commons/aop/LogAspect`

Ademas las excepciones deben generarse utilizando las siguientes clases `BusinessException`, `TechnicalException`
, `InternalException`, `ExternalException`, una personalizada como `CustomException` o `ValidateException` generada para
este servicio con un código de error y un mensaje que informe el error. En el caso que sea necesario tambien se puede
ingresar la excepcion capturada. De esta forma la clase LogAspect podra capturar el error y que el mismo se registre en
el servicio para que luego se envie a ElasticSearch de manera transparente.

* **Error**

El servicio tiene una estructura generica de respuesta para los casos de error, la misma esta compuesta de la siguiente
informacion:

* `resultCode`: Registra el codigo de error/ejecucion.
* `resultMessage`: Registra el mensaje de error/ejecucion.

Ejemplo:

````
{
    "resultCode": "100003",
    "resultMessage": "Error el campo Channel-Id es nulo o vacio"
}
````

* **Estructura de Logueo**

La estructura de logueo se define en la clase `LogAspect` en el constructor de la clase.

`LogUtil.initializeStructure(EnumSet.allOf(Logs.Header.class), EnumSet.allOf(Logs.Basic.class));`

Donde los Enums Logs.Header y Logs.Basic, poseen los siguientes valores:

````
    Logs.Header:
        TRANSACTION_ID,
        SESSION_ID,
        SERVICE_ID,
        CHANNEL_ID;
    Logs.Basic
        OPERATION,
        CODE,
        DESCRIPTION,
        ELAPSED,
        REQUEST,
        RESPONSE;
````

Dando asi por ejemplo esta estructura final de logueo:

`[TRANSACTION_ID=XX | SESSION_ID=XX | SERVICE_ID=XX | CHANNEL_ID=XX | OPERATION=XX | CODE=XX | DESCRIPTION=XX | ELAPSED=XX | REQUEST=XX | RESPONSE=XX]`

* **ElasticSearch**

El envio de los log a ElasticSearch se realiza a traves de la libreria `log4elk-spring-boot`, version `2.1.0`.

### Versiones

* **1.4.0 (Actual)**