## [1.5.7]

### Added

- Added support for `Curios` items to be charged via the Pocket Generator, thanks to @dbt

### Fixed

- KubeJS issues with recipe replacements when the server config is used in the result calculation. We now check the config is loaded before mutating our own recipe.

### Reminder

A reminder that in `1.5.6+` the `-common` config file no longer contains any options and all options have been moved to the `-server` config file. Please ensure you move any custom settings you had in the `-common` config to the `-server` config to ensure they are still applied.

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
