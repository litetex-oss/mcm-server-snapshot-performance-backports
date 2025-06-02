<!-- modrinth_exclude.start -->

[![Version](https://img.shields.io/modrinth/v/r2KV1Oja)](https://modrinth.com/mod/server-snapshot-performance-backports)
[![Build](https://img.shields.io/github/actions/workflow/status/litetex-oss/mcm-server-snapshot-performance-backports/check-build.yml?branch=dev)](https://github.com/litetex-oss/mcm-server-snapshot-performance-backports/actions/workflows/check-build.yml?query=branch%3Adev)

# Server Snapshot Performance Backports

<!-- modrinth_exclude.end -->

Backports server performance improvements for the latest release from the latest snapshots.

Details about the optimizations can be found in the [changelog](https://github.com/litetex-oss/mcm-server-snapshot-performance-backports/blob/dev/CHANGELOG.md).

## Motivation/Why does this mod exist?

Snapshots (e.g. [24w33a](https://minecraft.wiki/w/Java_Edition_24w33a)) can contain extreme performance improvements for servers.<br/>
However it may take months until we see these improvements finally shipped in a release.

This mod contains selected extracted improvements from the snapshots and backports them into the latest available release.

<details><summary>1.21.1 Idle CPU usage comparison</summary>

Before / Without mod:<br/>
![Before](https://raw.githubusercontent.com/litetex-oss/mcm-server-snapshot-performance-backports/refs/heads/dev/assets/1.21.1_Before_Without.jpg)

After / With mod:<br/>
![After](https://raw.githubusercontent.com/litetex-oss/mcm-server-snapshot-performance-backports/refs/heads/dev/assets/1.21.1_After_With.jpg)

</details>

<!-- modrinth_exclude.start -->

## Installation
[Installation guide for the latest release](https://github.com/litetex-oss/mcm-server-snapshot-performance-backports/releases/latest#Installation)

### Usage in other mods

Add the following to ``build.gradle``:
```groovy
dependencies {
    modImplementation 'net.litetex.mcm:server-snapshot-performance-backports:<version>'
    // Further documentation: https://wiki.fabricmc.net/documentation:fabric_loom
}
```

> [!NOTE]
> The contents are hosted on [Maven Central](https://repo.maven.apache.org/maven2/net/litetex/mcm/). You shouldn't have to change anything as this is the default maven repo.<br/>
> If this somehow shouldn't work you can also try [Modrinth Maven](https://support.modrinth.com/en/articles/8801191-modrinth-maven).

## Contributing
See the [contributing guide](./CONTRIBUTING.md) for detailed instructions on how to get started with our project.

<!-- modrinth_exclude.end -->
