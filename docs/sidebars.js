// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
  docs: [
    'intro',
    {
      type: 'category',
      label: 'Getting started',
      collapsed: false,
      items: ['getting-started/maven', 'getting-started/gradle', 'getting-started/generated-artifacts', 'getting-started/upgrading'],
    },
    {
      type: 'category',
      label: 'Configuration',
      collapsed: false,
      items: ['configuration/common', 'configuration/linux', 'configuration/macos', 'configuration/windows', 'configuration/manifest', 'configuration/file-associations'],
    },
    {
      type: 'category',
      label: 'Guides',
      collapsed: false,
      items: [
        'guides/bundling-a-jre',
        'guides/icons-and-assets',
        'guides/templates',
        'guides/runtime-jvm-options',
        'guides/multiple-executions',
        'guides/code-signing',
        'guides/windows-tools',
        'guides/snapshots',
      ],
    },
    {
      type: 'category',
      label: 'Samples',
      items: ['samples/maven', 'samples/gradle'],
    },
    'contributing',
  ],
};

export default sidebars;
