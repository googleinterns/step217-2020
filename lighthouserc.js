module.exports = {
    extends: 'lighthouse:default',
    ci: {
      collect: {
        url: ['http://localhost:8080/'],
        startServerCommand: 'mvn package appengine:run',
        onlyCategories: [
          'performance',
          'accessibility',
          'best-practices',
          'seo'
        ],
        skipAudits: [
          'unused-javascript',
          'uses-text-compression'
        ],
      },
      upload: {
        target: 'temporary-public-storage'
      },
      assert: {
        preset: 'lighthouse:no-pwa',
        assertions: {      
          'categories:performance': [
              'warn',
              { aggregationMethod: 'optimistic', minScore: 0.95}
          ],
          'categories:accessibility': [
              'warn', 
              { aggregationMethod: 'optimistic', minScore: 1}
          ],
          'categories:best-practices': [
              'warn',
              { aggregationMethod: 'optimistic', minScore: 1}
          ],
          'categories:seo': [
              'warn',
              { aggregationMethod: 'optimistic', minScore: 1}
          ],
          'unused-javascript': 'off',
          'uses-text-compression': 'off'
        }
      }
    },
  };
