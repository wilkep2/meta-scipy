inherit pypi setuptools3

SUMMARY = "SciPy: Scientific Library for Python"
HOMEPAGE = "https://www.scipy.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=8256119827cf2bbe63512d4868075867"

SRC_URI += " file://0001-Allow-passing-flags-via-FARCH-for-mach.patch"
SRC_URI[md5sum] = "293401ee7ac354a2f2313373b497f40e"
SRC_URI[sha256sum] = "4a453d5e5689de62e5d38edf40af3f17560bfd63c9c5bd228c18c1f99afa155b"

DEPENDS += " \
	${PYTHON_PN}-numpy \
	${PYTHON_PN}-numpy-native \
	${PYTHON_PN}-pybind11-native \
	lapack \
"

RDEPENDS:${PN} += " \
	${PYTHON_PN}-numpy \
	lapack \
"

CLEANBROKEN = "1"

export LAPACK = "${STAGING_LIBDIR}"
export BLAS = "${STAGING_LIBDIR}"

export F90 = "${TARGET_PREFIX}gfortran"
export F77 = "${TARGET_PREFIX}gfortran"
export FARCH = "${TUNE_CCARGS}"

export NUMPY_INCLUDE_PATH = "${STAGING_DIR_TARGET}${libdir}/python${PYTHON_BASEVERSION}/site-packages/numpy/core/include"

# Numpy expects the LDSHARED env variable to point to a single
# executable, but OE sets it to include some flags as well. So we split
# the existing LDSHARED variable into the base executable and flags, and
# prepend the flags into LDFLAGS
LDFLAGS:prepend := "${@" ".join(d.getVar('LDSHARED', True).split()[1:])} "
export LDSHARED := "${@d.getVar('LDSHARED', True).split()[0]}"

# Tell Numpy to look in target sysroot site-packages directory for libraries
LDFLAGS:append = " -L${STAGING_LIBDIR}/${PYTHON_DIR}/site-packages/numpy/core/lib"

INSANE_SKIP:${PN} = "already-stripped"
