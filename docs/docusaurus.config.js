// @ts-check
import {themes as prismThemes} from 'prism-react-renderer';

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'JavaPackager',
  tagline: 'Package Java apps as native Windows, macOS and GNU/Linux apps and installers, with Maven or Gradle',
  favicon: 'img/favicon.ico',

  headTags: [
    {tagName: 'link', attributes: {rel: 'icon', type: 'image/png', sizes: '32x32', href: '/JavaPackager/img/favicon-32x32.png'}},
    {tagName: 'link', attributes: {rel: 'icon', type: 'image/png', sizes: '192x192', href: '/JavaPackager/img/icon-192.png'}},
    {tagName: 'link', attributes: {rel: 'apple-touch-icon', sizes: '180x180', href: '/JavaPackager/img/apple-touch-icon.png'}},
  ],

  url: 'https://javapackager.github.io',
  baseUrl: '/JavaPackager/',
  organizationName: 'javapackager',
  projectName: 'JavaPackager',
  trailingSlash: false,

  onBrokenLinks: 'throw',
  markdown: {
    hooks: {
      onBrokenMarkdownLinks: 'throw',
    },
  },

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          // pages are in docs/content, not in Docusaurus' default docs/docs
          path: 'content',
          routeBasePath: '/',
          sidebarPath: './sidebars.js',
          editUrl: 'https://github.com/javapackager/JavaPackager/edit/devel/docs/',
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      image: 'img/social-card.png',
      colorMode: {
        respectPrefersColorScheme: true,
      },
      navbar: {
        title: 'JavaPackager',
        logo: {
          alt: 'JavaPackager',
          src: 'img/logo.png',
        },
        items: [
          {type: 'docSidebar', sidebarId: 'docs', position: 'left', label: 'Docs'},
          {href: 'https://github.com/javapackager/JavaPackager/releases', label: 'Releases', position: 'right'},
          {href: 'https://github.com/javapackager/JavaPackager', label: 'GitHub', position: 'right'},
        ],
      },
      footer: {
        style: 'dark',
        links: [
          {
            title: 'Docs',
            items: [
              {label: 'Getting started', to: '/getting-started/maven'},
              {label: 'Configuration', to: '/configuration/common'},
            ],
          },
          {
            title: 'Community',
            items: [
              {label: 'Issues', href: 'https://github.com/javapackager/JavaPackager/issues'},
              {label: 'Discussions', href: 'https://github.com/javapackager/JavaPackager/discussions'},
            ],
          },
          {
            title: 'More',
            items: [
              {label: 'GitHub', href: 'https://github.com/javapackager/JavaPackager'},
              {label: 'Maven Central', href: 'https://central.sonatype.com/artifact/io.github.javapackager/javapackager'},
            ],
          },
        ],
        copyright: `JavaPackager is released under the GPL-3.0 license. Built with Docusaurus.`,
      },
      prism: {
        theme: prismThemes.github,
        darkTheme: prismThemes.dracula,
        additionalLanguages: ['java', 'groovy', 'bash', 'powershell', 'ini', 'properties'],
      },
    }),
};

export default config;
