"""Dependency checks for JavaPackager's build.gradle.

Usage:
  python check_deps.py versions [build.gradle]   latest stable versions (same major line and overall)
  python check_deps.py bytecode <jar>...          highest class file version per jar (Java 8 = major 52)
"""
import re
import struct
import sys
import time
import urllib.request
import zipfile

MAVEN_CENTRAL = 'https://repo1.maven.org/maven2'
PLUGIN_PORTAL = 'https://plugins.gradle.org/m2'
UNSTABLE = re.compile(r'alpha|beta|rc|-m\d|milestone|snapshot|preview', re.I)


def fetch(url):
    # local DNS fails now and then: retry
    for attempt in range(5):
        try:
            with urllib.request.urlopen(url, timeout=20) as r:
                return r.read().decode('utf-8')
        except Exception:
            time.sleep(3)
    return None


def version_key(v):
    return [int(p) if p.isdigit() else p for p in re.split(r'[.\-]', v)]


def stable_versions(group, artifact):
    for repo in (MAVEN_CENTRAL, PLUGIN_PORTAL):
        xml = fetch(f"{repo}/{group.replace('.', '/')}/{artifact}/maven-metadata.xml")
        if xml:
            versions = [v for v in re.findall(r'<version>([^<]+)</version>', xml) if not UNSTABLE.search(v)]
            if versions:
                return sorted(versions, key=version_key)
    return []


def declared(build_gradle):
    text = open(build_gradle, encoding='utf-8').read()
    deps = re.findall(r"^\s*(\w+)\s+'([\w.\-]+):([\w.\-]+):([\w.\-]+)'", text, re.M)
    for conf, g, a, v in deps:
        yield conf, g, a, v
    for plugin_id, v in re.findall(r"id\s+'([\w.\-]+)'\s+version\s+'([\w.\-]+)'", text):
        yield 'plugin', plugin_id, f'{plugin_id}.gradle.plugin', v
    m = re.search(r"maven-plugin-plugin.*?=\s*'([\d.]+)'|mavenPluginPluginVersion\s*=\s*'([\d.]+)'", text)
    if m:
        yield 'descriptor', 'org.apache.maven.plugins', 'maven-plugin-plugin', m.group(1) or m.group(2)


def versions(build_gradle):
    print(f"{'configuration':<14} {'dependency':<62} {'current':<12} {'same major':<12} latest")
    for conf, g, a, v in declared(build_gradle):
        available = stable_versions(g, a)
        if not available:
            print(f'{conf:<14} {g + ":" + a:<62} {v:<12} {"?":<12} ?')
            continue
        major = v.split('.')[0]
        same = [x for x in available if x.split('.')[0] == major]
        same_major = same[-1] if same else '-'
        latest = available[-1]
        flag = '' if (same_major == v and latest == v) else '  <-- outdated'
        print(f'{conf:<14} {g + ":" + a:<62} {v:<12} {same_major:<12} {latest}{flag}')


def bytecode(jars):
    for path in jars:
        majors = set()
        with zipfile.ZipFile(path) as z:
            for name in z.namelist():
                if not name.endswith('.class') or name.startswith('META-INF/versions/') or name.endswith('module-info.class'):
                    continue
                head = z.read(name)[:8]
                if head[:4] == b'\xca\xfe\xba\xbe':
                    majors.add(struct.unpack('>H', head[6:8])[0])
        top = max(majors) if majors else None
        status = 'OK ' if top and top <= 52 else 'BAD'
        print(f"{status} major={top} (Java {top - 44 if top else '?'})  {path}")


if __name__ == '__main__':
    if len(sys.argv) >= 2 and sys.argv[1] == 'versions':
        versions(sys.argv[2] if len(sys.argv) > 2 else 'build.gradle')
    elif len(sys.argv) >= 3 and sys.argv[1] == 'bytecode':
        bytecode(sys.argv[2:])
    else:
        print(__doc__)
