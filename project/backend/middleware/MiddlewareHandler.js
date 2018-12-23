class MiddlewareHandler {
  constructor() {
    this.middlewares = [];
  }

  use(func) {
    this.middlewares.push(func);
  }

  executeMiddleware(data, done) {
    const composition = this.middlewares.reduceRight((done, next) => () => {
      next(data, done)
    }, done)
    
    composition(data);
  }

  run(data) {
    this.executeMiddleware(data, (result) => result);
  }
}

module.exports = MiddlewareHandler