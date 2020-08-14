module.exports = {
    ci: {
      collect: {
        url: ['http://localhost:3000/'],
        startServerCommand: 'mvn package appengine:run',
      },
      upload: {
        target: 'temporary-public-storage',
      },
    },
  };