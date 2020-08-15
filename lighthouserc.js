module.exports = {
    ci: {
      collect: {
        settings: {
          chromeFlags: '--no-sandbox'
        },
        url: ['http://localhost:8080/'],
        startServerCommand: 'mvn package appengine:run'
      },
      upload: {
        target: 'temporary-public-storage'
      },
    },
  };
