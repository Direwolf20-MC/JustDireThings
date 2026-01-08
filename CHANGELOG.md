## [1.5.6]

### Added

- Ukrainian translation by @WajtEvie thanks to #428

### Changed

*BREAKING CHANGE!*

- Moved all of the `-commmon` config options to the `-server` config file as this config is sync'd where as the `-common` config is not. Please move any custom settings you had in the `-common` config to the `-server` config to ensure they are still applied. (Thanks to #456 for pointing out this issue)

- Updated `PT_BR` translations thanks to #436 by @PrincessStelllar
- Updated `zh_cn` translations thanks to #386 by @ChuijkYahus

### Fixed

- `SWAPPERDENY` / `RELOCATION_NOT_SUPPORTED` being ignored when swapping from a dimension other than the overworld. Fixes #472
- Crash when placing water bucket in very specific conditions. Added a null check to prevent it. Fixes #445
