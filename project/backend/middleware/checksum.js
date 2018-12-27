const isJSON = require('validator/lib/isJSON');
const { isValidChecksum } = require('../common/md5checksum');

module.exports = (ctx, next) => {
  const checksum = ctx.data.slice(0, 32);

  if (checksum.length < 32) {
    const error = new Error('Data needs to be a string of the structure "[checksum][dataAsJSON]"');
    error.code = 'INVALID_CHECKSUM';

    throw error;
  }

  const dataJSON = ctx.data.slice(32);

  if (!isJSON(dataJSON)) {
    const error = new Error('Data payload was not valid JSON');
    error.code = 'INVALID_JSON';

    throw error;
  }

  if (!isValidChecksum(checksum, dataJSON)) {
    const error = new Error('Checksum did not match the JSON data');
    error.code = 'INVALID_CHECKSUM';

    throw error;
  }

  ctx.data = JSON.parse(dataJSON);

  next();
};
