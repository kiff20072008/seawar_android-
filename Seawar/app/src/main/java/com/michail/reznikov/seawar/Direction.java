package com.michail.reznikov.seawar;

public enum Direction {
    DOWN{
        @Override
        public Direction opposite()
        {
            return UP;
        }
        @Override
        public Direction next()
        {
            return LEFT;
        }
        @Override
        public Direction prev()
        {
            return RIGHT;
        }
    },LEFT{
        @Override
        public Direction opposite()
        {
            return RIGHT;
        }
        @Override
        public Direction next()
        {
            return UP;
        }
        @Override
        public Direction prev()
        {
            return DOWN;
        }
    },UP{
        @Override
        public Direction opposite()
        {
            return DOWN;
        }
        @Override
        public Direction next()
        {
            return RIGHT;
        }
        @Override
        public Direction prev()
        {
            return LEFT;
        }
    },
    RIGHT{
        @Override
        public Direction opposite()
        {
            return LEFT;
        }
        @Override
        public Direction next()
        {
            return DOWN;
        }
        @Override
        public Direction prev()
        {
            return UP;
        }
    };
    public abstract Direction opposite();
    public abstract Direction next();
    public abstract Direction prev();

}
