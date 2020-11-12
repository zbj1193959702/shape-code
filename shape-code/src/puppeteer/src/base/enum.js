const PLATFORM = {
    LianCang: 1,
    zgzs: 2,
    cfw: 3,
    TongCheng: 4,
};

const USE_TYPE = {
    cangku: 1,
    changfang: 2,
};

const POST_STATUS = {
    undone: 0,
    done: 1,
}

const TASK_TYPE = {
    POST: 1,
    REFRESH: 2
}

// @com.clinks.project.enums.RentalUnitRentalDuration
const RENTAL_DURATION = {
    ONE_MONTH: 0,
    THREE_MONTH: 1,
    SIX_MONTH: 2,
    ONE_YEAR: 3,
    TWO_YEAR: 4,
    THREE_YEAR: 5,
    FOUR_YEAR: 6,
    FIVE_YEAR: 7,
};

const ONLINE_STATUS = {
    ONLINE : 1,
    OFFLINE : 0,
}

module.exports = {
    PLATFORM,
    USE_TYPE,
    TASK_TYPE,
    POST_STATUS,
    RENTAL_DURATION,
    ONLINE_STATUS
};
